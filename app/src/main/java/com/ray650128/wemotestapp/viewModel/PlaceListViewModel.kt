package com.ray650128.wemotestapp.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.ray650128.wemotestapp.db.asLiveData
import com.ray650128.wemotestapp.model.Place
import io.realm.Realm
import io.realm.Sort


class PlaceListViewModel : ViewModel() {

    private val realm: Realm by lazy { Realm.getDefaultInstance() }

    private val _listData = realm.where(Place::class.java).sort("timestamp", Sort.DESCENDING).findAllAsync().asLiveData()

    val listData: LiveData<List<Place>> = Transformations.map(_listData) { realmResult ->
        realm.copyFromRealm(realmResult)
    }

    fun addData(item: Place) = realm.executeTransactionAsync {
        val maxId = it.where(Place::class.java).max("id")
        val nextId = if (maxId == null) 1 else maxId.toInt() + 1

        it.createObject(Place::class.java, nextId).apply {
            title = item.title
            content = item.content
            timestamp = item.timestamp
            photos = item.photos
            latitude = item.latitude
            longitude = item.longitude
        }
    }

    fun updateData(item: Place) = realm.executeTransactionAsync {
        it.copyToRealmOrUpdate(item)
    }

    fun deleteData(id: Int) = realm.executeTransaction {
        it.where(Place::class.java).equalTo("id", id).findFirst()?.deleteFromRealm()
    }

    override fun onCleared() {
        realm.close()
        super.onCleared()
    }
}