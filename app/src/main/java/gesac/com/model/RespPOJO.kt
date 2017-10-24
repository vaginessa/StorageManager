package gesac.com.model

/**
 * Created by GE11522 on 2017/10/9.
 */
class RespPOJO<T> {
    var code: Int = 0
    lateinit var msg: String
    var data: T? = null


    constructor(code: Int, msg: String, data: T) : super() {
        this.code = code
        this.msg = msg
        this.data = data
    }

    constructor() {

    }
}