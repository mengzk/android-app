package com.dx.health.module.libs.jtp

import com.jinmuhealth.bluetooth.session.Device
import com.jinmuhealth.bluetooth.session.IDeviceFilter

/**
 * JM固件过滤器
 *
 */
internal class JTFilter constructor(

    /**
     * 按设备name进行过滤的前缀
     */
    private val namePrefix: String = "JT"

) : IDeviceFilter {

    override fun match(device: Device): Boolean {
        if (device.name!!.startsWith(namePrefix)) {
            return true
        }

        return false
    }
}
