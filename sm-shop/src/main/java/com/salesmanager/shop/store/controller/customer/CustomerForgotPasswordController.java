package com.salesmanager.shop.store.controller.customer;

import com.salesmanager.core.business.exception.ServiceException;
import com.salesmanager.core.business.services.catalog.product.PricingService;
import com.salesmanager.core.business.services.merchant.MerchantStoreService;
import com.salesmanager.core.business.services.reference.country.CountryService;
import com.salesmanager.core.business.services.reference.language.LanguageService;
import com.salesmanager.core.business.services.reference.zone.ZoneService;
import com.salesmanager.core.business.services.shoppingcart.ShoppingCartCalculationService;
import com.salesmanager.core.business.services.system.EmailService;
import com.salesmanager.core.model.merchant.MerchantStore;
import com.salesmanager.core.model.reference.language.Language;
import com.salesmanager.shop.constants.Constants;
import com.salesmanager.shop.store.api.exception.RestApiException;
import com.salesmanager.shop.store.controller.AbstractController;
import com.salesmanager.shop.store.controller.ControllerConstants;
import com.salesmanager.shop.store.controller.customer.facade.CustomerFacade;
import com.salesmanager.shop.store.security.PasswordRequest;
import com.salesmanager.shop.utils.CaptchaRequestUtils;
import com.salesmanager.shop.utils.EmailTemplatesUtils;
import com.salesmanager.shop.utils.ImageFilePath;
import com.salesmanager.shop.utils.LabelUtils;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import springfox.documentation.annotations.ApiIgnore;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Locale;


/**
 * Customer reset his password
 * @author Guy Moyo
 *
 */



@Controller
@RequestMapping("/shop/customer")
public class CustomerForgotPasswordController extends AbstractController {

	private static final Logger LOGGER = LoggerFactory.getLogger(CustomerForgotPasswordController.class);

	@Inject
	private com.salesmanager.shop.store.controller.customer.facade.v1.CustomerFacade customerFacade;

	@Inject
	MerchantStoreService storeService;

    @RequestMapping(value="/forgotPassword.html", method=RequestMethod.GET)
    public String showForgotPasswordForm(HttpServletRequest request) {

		MerchantStore store = getSessionAttribute(Constants.MERCHANT_STORE, request);
		StringBuilder template = new StringBuilder().append(ControllerConstants.Tiles.Customer.forgotPassword).append(".").append(store.getStoreTemplate());

		return template.toString();

	}

	/**
	 * Verify a password token
	 * @param store
	 * @param token
	 * @param merchantStore
	 * @param language
	 */
	@ResponseStatus(HttpStatus.OK)
	@GetMapping(value = { "/{store}/reset/{token}" })
	public String passwordResetVerify(@PathVariable String store, @PathVariable String token,
									  @ApiIgnore MerchantStore merchantStore, @ApiIgnore Language language,
									  Model model, HttpServletRequest request) {

		/**
		 * Receives reset token Needs to validate if user found from token Needs
		 * to validate if token has expired
		 *
		 * If no problem void is returned otherwise throw OperationNotAllowed
		 * All of this in UserFacade
		 */

		LOGGER.info("verify token: "+token);
		customerFacade.verifyPasswordRequestToken(token, store);

		MerchantStore merchStore = null;
		try {
			merchStore = storeService.getByCode(store);
		} catch (ServiceException e) {
			throw new IllegalArgumentException(e.getMessage());
		}
		StringBuilder template = new StringBuilder().append(ControllerConstants.Tiles.Customer.resetPassword).append(".").append(merchStore.getStoreTemplate());
		model.addAttribute("passwordRequest", new PasswordRequest());
		model.addAttribute("store", store);
		model.addAttribute("token", token);

		return template.toString();

	}

	/**
	 * Change password
	 * @param passwordRequest
	 * @param store
	 * @param token
	 * @param merchantStore
	 * @param language
	 * @param request
	 */
	@RequestMapping(value = "/{store}/password/{token}", method = RequestMethod.POST)
	public String changePassword(
			@ModelAttribute("passwordRequest") @Valid PasswordRequest passwordRequest,
			@PathVariable String store, @PathVariable String token, @ApiIgnore MerchantStore merchantStore,
			@ApiIgnore Language language, BindingResult bindingResult, Model model,
			HttpServletRequest request, HttpServletResponse response, final Locale locale,
			RedirectAttributes redirectAttributes) {

		// validate password
		if (StringUtils.isBlank(passwordRequest.getPassword())
				|| StringUtils.isBlank(passwordRequest.getRepeatPassword())) {

			return populateModel(model, "Password don't match", store, token);
		}

		if (!passwordRequest.getPassword().equals(passwordRequest.getRepeatPassword())) {
			return populateModel(model, "Password don't match", store, token);
		}

		try {
			customerFacade.resetPassword(passwordRequest.getPassword(), token, store);
		} catch (Exception e) {
			return populateModel(model, e.getMessage(), store, token);
		}

		return "redirect:/shop/customer/customLogon.html";
	}

	private String populateModel(Model model, String msg, String store, String token) {

		MerchantStore merchStore = null;
		try {
			merchStore = storeService.getByCode(store);
		} catch (ServiceException e) {
			throw new IllegalArgumentException(e.getMessage());
		}
		StringBuilder template = new StringBuilder().append(ControllerConstants.Tiles.Customer.resetPassword).append(".").append(merchStore.getStoreTemplate());
		model.addAttribute("passwordRequest", new PasswordRequest());
		model.addAttribute("store", store);
		model.addAttribute("token", token);
		model.addAttribute("error", msg);

		return template.toString();
	}

}
