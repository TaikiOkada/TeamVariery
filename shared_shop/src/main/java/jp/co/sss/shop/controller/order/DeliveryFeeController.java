package jp.co.sss.shop.controller.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jp.co.sss.shop.repository.FeeRepository;

@Controller

public class DeliveryFeeController {
	@Autowired
	FeeRepository feeRepository;

	@RequestMapping(path="/confirm" ,method =RequestMethod.GET )
	public String showDeliveryFee(Model model) {
			model.addAttribute("delivery_fee",feeRepository.findAll());
			return "confirm/Confirm";
	}

}
