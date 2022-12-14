package com.subteno.vuo.Controller;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.concurrent.ExecutionException;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.subteno.vuo.Model.Item;
import com.subteno.vuo.Model.Result;
import com.subteno.vuo.Model.Role;
import com.subteno.vuo.Model.TxOrder;
import com.subteno.vuo.Model.User;
import com.subteno.vuo.Services.ItemServices;
import com.subteno.vuo.Services.OrderServices;
import com.subteno.vuo.Services.UserServices;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class VuoController {

    private final UserServices userService;
    private final ItemServices itemServices;
    private final OrderServices orderServices;

    // PAGES
    @GetMapping(value = { "/", "/assignrole" })
    public String index() {
        return "main";
    }

    @GetMapping("/add")
    public String addPage(Model model) {
        if (!model.containsAttribute("user"))
            model.addAttribute("user", new User());
        return "main";
    }

    @GetMapping("/additem")
    public String addItemPage(Model model) {
        if (!model.containsAttribute("item"))
            model.addAttribute("item", new Item());
        return "main";
    }

    @PostMapping("/getitem")
    public String getItemPage(String itemName, RedirectAttributes rattrs)
            throws InterruptedException, ExecutionException {
        if (itemName.isEmpty()) {
            rattrs.addFlashAttribute("error", "Item is empty");
            return "redirect:/additem";
        }
        Result res = getItem(itemName).getBody();
        if (res.getMessage().equals("error")) {
            rattrs.addFlashAttribute("error", res.getObject());
            return "redirect:/additem";
        } else {
            rattrs.addFlashAttribute("item", res.getObject());
            return "redirect:/additem";
        }
    }

    @PostMapping("/deleteitem")
    public String deleteItemPage(String name, RedirectAttributes rattrs) throws Exception {
        if (name.isEmpty()) {
            rattrs.addFlashAttribute("error", "Item is empty");
            return "redirect:/additem";
        }
        Result res = deleteItem(name).getBody();
        rattrs.addFlashAttribute(res.getMessage(), res.getObject());
        return "redirect:/additem";
    }
    // APIS

    @PostMapping("/api/assignrole")
    public String assignRoleUser(String username, Role role, RedirectAttributes rattrs) throws Exception {
        Result result = userService.AssignRoleUser(username, role);
        rattrs.addFlashAttribute(result.getMessage(), result.getObject());
        return "redirect:/assignrole";
    }

    @PostMapping("/api/add")
    public String addUserData(@Valid User user, BindingResult bresult, RedirectAttributes rattrs)
            throws InterruptedException, ExecutionException {
        if (bresult.hasErrors()) {
            rattrs.addFlashAttribute("org.springframework.validation.BindingResult.user", bresult);
            rattrs.addFlashAttribute("user", user);
            return "redirect:/add";
        }
        Result result = userService.AddUser(user);
        rattrs.addFlashAttribute(result.getMessage(), result.getObject());
        return "redirect:/add";
    }

    @PostMapping("/api/get")
    public String getData(String username, RedirectAttributes rattrs) throws InterruptedException, ExecutionException {
        Result result = userService.GetUser(username);
        rattrs.addFlashAttribute(result.getMessage(), result.getObject());
        return "redirect:/add";
    }

    @PostMapping("/api/delete")
    public String deleteData(User user, BindingResult bresult, RedirectAttributes rattrs)
            throws InterruptedException, ExecutionException {
        Result result = userService.DeleteUser(user);
        rattrs.addFlashAttribute(result.getMessage(), result.getObject());
        return "redirect:/add";
    }

    @PostMapping("/api/additem")
    public String addItem(MultipartFile file, @Valid Item item, BindingResult bresult,
            RedirectAttributes rattrs)
            throws IOException, URISyntaxException, InterruptedException, ExecutionException {
        if (bresult.hasErrors()) {
            rattrs.addFlashAttribute("org.springframework.validation.BindingResult.item", bresult);
            rattrs.addFlashAttribute("item", item);
            return "redirect:/additem";
        }
        if (file.getSize() > 1048576) {
            rattrs.addFlashAttribute("error", "File size exceeded maximum");
            return "redirect:/additem";
        }
        if (!file.getContentType().contains("image")) {
            rattrs.addFlashAttribute("error", "File is not an image");
            return "redirect:/additem";
        }
        Result result = itemServices.AddItem(item, file);
        rattrs.addFlashAttribute(result.getMessage(), result.getObject());
        return "redirect:/additem";
    }

    @GetMapping("/api/getitem")
    public ResponseEntity<Result> getItem(String itemName) throws InterruptedException, ExecutionException {
        return ResponseEntity.ok().body(itemServices.GetItem(itemName));
    }

    // TO DO : SEE OUTPUT
    @GetMapping("/api/getitems")
    public ResponseEntity<Result> getItems(Integer offset, Integer limit) throws Exception {
        if (offset == null || limit == null) {
            return ResponseEntity.badRequest().body(new Result("error", "Offset / limit is null"));
        }
        return ResponseEntity.ok(itemServices.GetItems(offset, limit));
    }

    @PostMapping("/api/deleteitem")
    public ResponseEntity<Result> deleteItem(String itemName) throws Exception {
        return ResponseEntity.ok(itemServices.DeleteItem(itemName));
    }

    @PostMapping("/api/addorder")
    public ResponseEntity<Result> addOrder(TxOrder order) throws Exception {
        return ResponseEntity.ok(orderServices.OrderItem(order));
    }

}
