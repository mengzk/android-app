package com.dxm.aimodel.modules.record;

/**
 * Author: Meng
 * Date: 2024/03/28
 * Modify: 2024/03/28
 * Desc:
 */
public interface RecordStreamListener {
    void recordOfByte(byte[] data, int begin, int end);
}
