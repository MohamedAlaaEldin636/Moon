package grand.app.moon.extensions

import androidx.paging.PagingData
import androidx.paging.TerminalSeparatorType
import androidx.paging.insertHeaderItem

fun <T : Any> PagingData<T>.insertHeaderItems(
	items: List<T>,
	terminalSeparatorType: TerminalSeparatorType = TerminalSeparatorType.FULLY_COMPLETE,
): PagingData<T> {
	var pagingData = this
	for (item in items.reversed()) {
		pagingData = pagingData.insertHeaderItem(terminalSeparatorType, item)
	}
	return pagingData
}




