package com.dx.health.model.entity

/**
 * Author: Meng
 * Date: 2024/11/21
 * Modify: 2024/11/21
 * Desc:
 */
class HomeItem(
    var name: String?, var desc: String?, var icon: String,
    var id: Int
) {
    constructor(name: String?, desc: String?, id: Int) : this(name, desc, "", id)
}
