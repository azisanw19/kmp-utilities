package id.feinn.utility.context

public actual abstract class FeinnLocalContext

internal class FeinnLocalContextImpl : FeinnLocalContext() {
    companion object {
        val instance = FeinnLocalContextImpl()
    }
}