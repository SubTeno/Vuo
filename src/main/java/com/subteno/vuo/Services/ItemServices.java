package com.subteno.vuo.Services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firebase.cloud.StorageClient;
import com.subteno.vuo.Interface.ItemInterfaceService;
import com.subteno.vuo.Model.Item;
import com.subteno.vuo.Model.Result;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ItemServices implements ItemInterfaceService {

    private static final String COLLECTION_NAME = "vuoItem";
    private CollectionReference collection = FirestoreClient.getFirestore().collection(COLLECTION_NAME);
    private StorageClient storageClient = StorageClient.getInstance();

    @Override
    public Result GetItem(String itemName) throws InterruptedException, ExecutionException {
        DocumentSnapshot doc = collection.document(itemName).get().get();
        if (!doc.exists()) {
            return new Result("error", "Item doesn't exist");
        }
        return new Result("success", doc.toObject(Item.class));
    }

    @Override
    public Result AddItem(Item item, MultipartFile file) throws InterruptedException, ExecutionException, IOException {
        item.setLink(storageClient.bucket().create(
                item.getName() + "." + StringUtils.getFilenameExtension(file.getOriginalFilename()), file.getBytes(),
                file.getContentType()).getMediaLink());
        return new Result("success", "Success. Timestamp : "
                + collection.document(item.getName()).set(item).get().getUpdateTime().toString());
    }

    @Override
    public Result GetItems(Integer offset, Integer limit) throws Exception {
        List<Item> items = new ArrayList<Item>();
        collection.offset(offset).limit(limit).get().get().getDocuments().forEach(item -> {
            items.add(item.toObject(Item.class));
        });
        return new Result("success", items);
    }

    @Override
    public Result DeleteItem(String itemName) throws Exception {
        DocumentReference doc = collection.document(itemName);
        if (!doc.get().get().exists())
            return new Result("error", "Item doesn't exist");
        return new Result("success", doc.delete().get().getUpdateTime().toString());
    }

}
