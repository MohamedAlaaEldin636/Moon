package grand.app.moon.data.shop

import grand.app.moon.data.remote.BaseRemoteDataSource
import grand.app.moon.presentation.myStore.ItemWorkingHours2
import javax.inject.Inject

class ShopRemoteDataSource @Inject constructor(private val apiService: ShopServices) : BaseRemoteDataSource() {

	suspend fun getMyCategories(page: Int) = safeApiCall2 {
		apiService.getMyCategories(page)
	}
	suspend fun deleteCategory(id: Int) = safeApiCall {
		apiService.deleteCategory(id)
	}
	suspend fun createCategory(name: String) = safeApiCall {
		apiService.createCategory(name)
	}
	suspend fun updateCategory(id: Int, name: String) = safeApiCall {
		apiService.updateCategory(id, name)
	}

	suspend fun getMySubCategories(page: Int) = safeApiCall2 {
		apiService.getMySubCategories(page)
	}
	suspend fun deleteSubCategory(id: Int) = safeApiCall {
		apiService.deleteSubCategory(id)
	}
	suspend fun createSubCategory(name: String, parentId: Int) = safeApiCall {
		apiService.createSubCategory(name, parentId)
	}
	suspend fun updateSubCategory(id: Int, name: String, parentId: Int) = safeApiCall {
		apiService.updateSubCategory(id, name, parentId)
	}

	suspend fun getWorkingHours() = safeApiCall {
		apiService.getWorkingHours()
	}

	suspend fun saveWorkingHours(list: List<ItemWorkingHours2>) = safeApiCall {
		val map = mutableMapOf<String, String>()

		for ((index, item) in list.withIndex()) {
			map["working_hours[$index][from]"] = item.from
			map["working_hours[$index][to]"] = item.to
			map["working_hours[$index][status]"] = item.enabled.toString()
		}

		apiService.saveWorkingHours(map)
	}

}
