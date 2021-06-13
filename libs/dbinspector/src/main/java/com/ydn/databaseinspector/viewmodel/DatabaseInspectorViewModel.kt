package com.ydn.databaseinspector.viewmodel

import androidx.lifecycle.*
import androidx.paging.cachedIn
import com.ydn.databaseinspector.data.DatabaseInspectorRepository
import com.ydn.databaseinspector.data.Database
import com.ydn.databaseinspector.data.TableInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DatabaseInspectorViewModel @Inject constructor(
    private val repository: DatabaseInspectorRepository
): ViewModel() {

    var databases = MutableLiveData<List<Database>>()

    suspend fun loadDatabases() = viewModelScope.launch {
        if (databases.value != null) {
            for (db in databases.value!!) {
               db.sqLiteDatabase.close()
            }
        }
        databases.postValue(repository.getDatabases())
    }

    fun loadTableData(database: Database, table: TableInfo) =
        repository.getTableDataStream(database, table).cachedIn(viewModelScope)
}


