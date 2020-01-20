package com.aiqency.tmdb.realms

import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import io.realm.Realm
import io.realm.RealmModel
import io.realm.RealmQuery
import io.realm.RealmResults

fun realmUse(realm: Realm = Realm.getDefaultInstance(), f: Realm.() -> Unit) = realm.use { realm.f() }

fun realmTransaction(realm: Realm = Realm.getDefaultInstance(), f: Realm.() -> Unit) =
    realm.executeTransaction { realm.f() }

fun realmTransactionAsync(
    realm: Realm = Realm.getDefaultInstance(),
    f: Realm.() -> Unit,
    onSuccess:  SimpleListener,
    onError:  GenericListener<Throwable>
) = realm.executeTransactionAsync({ realm.f() }, { onSuccess.invoke() }, { onError.invoke(it) })

fun Observable<MoviesResponse>.async() = subscribeOn(Schedulers.io())

fun <T: RealmModel> realmGetList(
    clazz: Class<T>,
    realm: Realm = Realm.getDefaultInstance(),
    f: (RealmQuery<T>.() -> RealmQuery<T>)? = null
): Pair<Realm, RealmResults<T>> = Pair(realm, (if (f != null)
    realm.where(clazz).f() else realm.where(clazz)).findAll() )

fun <T: RealmModel> realmGetFirst(
    clazz: Class<T>,
    realm: Realm = Realm.getDefaultInstance(),
    f: (RealmQuery<T>.() -> RealmQuery<T>)? = null
): Pair<Realm, T?> = Pair(realm, (if (f != null)
    realm.where(clazz).f() else realm.where(clazz)).findFirst() )

fun <T: RealmModel> realmHas(
    clazz: Class<T>,
    realm: Realm = Realm.getDefaultInstance()
): Boolean {
    var has = false
    realmTransaction { has = realm.where(clazz).findFirst() != null }
    return has
}


fun realmPersist(model: Any, realm: Realm = Realm.getDefaultInstance()) =
    when (model) {
        is RealmModel -> realmTransaction(realm) { copyToRealmOrUpdate(model) }
        is List<*> -> realmTransaction(realm) { copyToRealmOrUpdate(model.filterIsInstance(RealmModel::class.java)) }
        else -> throw IllegalStateException("Only realm model can be persisted")
    }

fun realmPersistAsync(
    model: Any,
    realm: Realm = Realm.getDefaultInstance(),
    onSuccess:  SimpleListener,
    onError:  GenericListener<Throwable>
) = when (model) {
        is RealmModel -> realmTransactionAsync(realm, { copyToRealmOrUpdate(model) }, onSuccess, onError)
        is List<*> -> realmTransactionAsync(realm, { copyToRealmOrUpdate(model.filterIsInstance(RealmModel::class.java)) }, onSuccess, onError)
        else -> throw IllegalStateException("Only realm model can be persisted")
    }