package com.subteno.vuo.Interface;

import com.subteno.vuo.Model.Result;
import com.subteno.vuo.Model.TxOrder;

public interface OrderInterfaceService {
    Result OrderItem(TxOrder TxOrder) throws Exception;
}
