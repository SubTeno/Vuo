package com.subteno.vuo.Services;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

import org.springframework.stereotype.Service;

import com.google.cloud.firestore.CollectionReference;
import com.google.firebase.cloud.FirestoreClient;
import com.subteno.vuo.Interface.OrderInterfaceService;
import com.subteno.vuo.Model.Result;
import com.subteno.vuo.Model.TxOrder;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderServices implements OrderInterfaceService {

    private static final String COLLECTION_NAME = "vuoOrder";
    private CollectionReference collection = FirestoreClient.getFirestore().collection(COLLECTION_NAME);

    @Override
    public Result OrderItem(TxOrder TxOrder) throws InterruptedException, ExecutionException {
        TxOrder.setUID(UUID.randomUUID().toString());
        return new Result("sucess", collection.document(TxOrder.getUID()).set(TxOrder).get().getUpdateTime().toString());
    }
    
}
