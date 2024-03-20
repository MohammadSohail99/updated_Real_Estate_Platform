package com.example.Real_Estate_Platform.Controller;

import com.example.Real_Estate_Platform.Entity.Buyer;
import com.example.Real_Estate_Platform.Entity.Property;
import com.example.Real_Estate_Platform.Entity.Seller;
import com.example.Real_Estate_Platform.Model.MediatorModel;
import com.example.Real_Estate_Platform.Service.BuyerService;
import com.example.Real_Estate_Platform.Service.MediatorService;
import com.example.Real_Estate_Platform.Service.PropertyService;
import com.example.Real_Estate_Platform.Service.SellerService;
import com.example.Real_Estate_Platform.validation.ValidationMediator;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
public class MediatorController {
    @Autowired
    private MediatorService mediatorService;
    @Autowired
    private SellerService sellerService;
    @Autowired
    private BuyerService buyerService;
    @Autowired
    private PropertyService propertyService;
    @Autowired
    ValidationMediator validationMediator;
    @RequestMapping("/")
    public String start(){
        return "start";
    }
        @RequestMapping("/registers")
        public String showRegisterForm(Model model) {
            MediatorModel mediatorModel=new MediatorModel();
            model.addAttribute("mediator",mediatorModel);
            return "registerForm";
        }
        @RequestMapping("/register")
        public String registerMediator(@Valid  @ModelAttribute("mediator") MediatorModel mediatorModel, BindingResult bindingResult) {
            validationMediator.validate(mediatorModel,bindingResult);
            if(bindingResult.hasErrors()){
                return "registerForm";
            }
            mediatorService.registerMediator(mediatorModel);
            return "redirect:/loginM";
        }
        @RequestMapping("/loginM")
        public String showLoginForm(Model model) {
            MediatorModel mediatorModel=new MediatorModel();
            model.addAttribute("mediator",mediatorModel);
            return "loginForm";
        }

        @RequestMapping("/loginMediator")
        public String loginMediator(@RequestParam String username, @RequestParam String password,Model model){
            MediatorModel mediatorModel = mediatorService.loginMediator(username, password);
            if (mediatorModel != null) {
                model.addAttribute("mediator", mediatorModel);
                model.addAttribute("mid",mediatorModel.getMid());
                return "redirect:/viewAllSeller";
            } else {
                model.addAttribute("loginFailed", true);
                return "notaccept";
            }
        }
    @RequestMapping("/viewAllSeller")
    public String getAllSellers( Model model) {
        List<Seller> sellers= sellerService.getAllSellers();
        model.addAttribute("sellers", sellers);
        return "viewAllSellers";
    }
    @RequestMapping("/viewBuyer")
    public String viewBuyerInfo(@RequestParam("propertyId") int propertyId, Model model) {
        Property property = propertyService.getPropertyById(propertyId);
        Buyer buyer = property.getBuyer();
        model.addAttribute("buyer", buyer);
        model.addAttribute("property",property);
        double total=propertyService.calculate(property.getArea(),property.getPrice(),propertyId);
        model.addAttribute("total",total);
        return "viewAllBuyers";
    }
    @GetMapping("/viewPropertiesBySeller")
    public String viewPropertiesBySeller(@RequestParam("sellerId") int sellerId, Model model) {
        List<Property> properties = propertyService.getPropertiesBySellerId(sellerId);
        model.addAttribute("properties", properties);
        return "viewPropertySeller";
    }


}








