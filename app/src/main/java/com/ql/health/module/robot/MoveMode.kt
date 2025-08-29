package com.ql.health.module.robot

/**
 * Author: Meng
 * Date: 2024/12/22
 * Desc:
 *     headUp ：抬头
 *     headDown ：低头
 *     headLeft ：左转头
 *     headRight ：右转头
 *     bodyForward ：前进
 *     bodyBack ：后退
 *     bodyLeft ：左转
 *     bodyRight ：右转
 */
enum class MoveMode(mode: String) {
    HeadUp("headUp"),HeaDown("headDown"),HeadLeft("headLeft"),HeadRight("headRight"),
    Forward("bodyForward"),Back("bodyBack"),Left("bodyLeft"),Right("bodyRight");
}