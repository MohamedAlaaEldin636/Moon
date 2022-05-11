package grand.app.moon.helpers.paging

class MAPagingException(val failure: MAResult.Failure<*>) : Exception(failure.message)
