package id.feinn.utility.time.exception

public class FeinnDateTimeThrowable : Throwable {

    public constructor(message: String) : super(message)

    public constructor(message: String, cause: Throwable) : super(message, cause)

    public constructor(cause: Throwable) : super(cause)

    public constructor() : super()

    public companion object {}

}