package com.salesmanager.core.business.services.reference.init;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.salesmanager.core.business.exception.ServiceException;
import com.salesmanager.core.business.services.catalog.category.CategoryService;
import com.salesmanager.core.business.services.catalog.product.manufacturer.ManufacturerService;
import com.salesmanager.core.business.services.catalog.product.type.ProductTypeService;
import com.salesmanager.core.business.services.merchant.MerchantStoreService;
import com.salesmanager.core.business.services.reference.country.CountryService;
import com.salesmanager.core.business.services.reference.currency.CurrencyService;
import com.salesmanager.core.business.services.reference.language.LanguageService;
import com.salesmanager.core.business.services.reference.loader.IntegrationModulesLoader;
import com.salesmanager.core.business.services.reference.loader.ZonesLoader;
import com.salesmanager.core.business.services.reference.zone.ZoneService;
import com.salesmanager.core.business.services.system.ModuleConfigurationService;
import com.salesmanager.core.business.services.system.optin.OptinService;
import com.salesmanager.core.business.services.tax.TaxClassService;
import com.salesmanager.core.business.services.user.GroupService;
import com.salesmanager.core.business.services.user.PermissionService;
import com.salesmanager.core.business.utils.SecurityGroupsBuilder;
import com.salesmanager.core.constants.SchemaConstant;
import com.salesmanager.core.model.catalog.category.Category;
import com.salesmanager.core.model.catalog.category.CategoryDescription;
import com.salesmanager.core.model.catalog.product.manufacturer.Manufacturer;
import com.salesmanager.core.model.catalog.product.manufacturer.ManufacturerDescription;
import com.salesmanager.core.model.catalog.product.type.ProductType;
import com.salesmanager.core.model.merchant.MerchantStore;
import com.salesmanager.core.model.reference.country.Country;
import com.salesmanager.core.model.reference.country.CountryDescription;
import com.salesmanager.core.model.reference.currency.Currency;
import com.salesmanager.core.model.reference.language.Language;
import com.salesmanager.core.model.reference.zone.Zone;
import com.salesmanager.core.model.reference.zone.ZoneDescription;
import com.salesmanager.core.model.system.IntegrationModule;
import com.salesmanager.core.model.system.optin.Optin;
import com.salesmanager.core.model.system.optin.OptinType;
import com.salesmanager.core.model.tax.taxclass.TaxClass;
import com.salesmanager.core.model.user.Group;
import com.salesmanager.core.model.user.GroupType;
import com.salesmanager.core.model.user.Permission;

@Service("initializationDatabase")
public class InitializationDatabaseImpl implements InitializationDatabase {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(InitializationDatabaseImpl.class);
	

	@Inject
	private ZoneService zoneService;
	
	@Inject
	private LanguageService languageService;
	
	@Inject
	private CountryService countryService;
	
	@Inject
	private CurrencyService currencyService;
	
	@Inject
	protected MerchantStoreService merchantService;
		
	@Inject
	protected ProductTypeService productTypeService;
	
	@Inject
	private TaxClassService taxClassService;
	
	@Inject
	private ZonesLoader zonesLoader;
	
	@Inject
	private IntegrationModulesLoader modulesLoader;
	
	@Inject
	private ManufacturerService manufacturerService;
	
	@Inject
	private	CategoryService categoryService;
	
	@Inject
	private ModuleConfigurationService moduleConfigurationService;
	
	@Inject
	private OptinService optinService;
	
	@Inject
	protected GroupService   groupService;
	
	@Inject
	protected PermissionService   permissionService;

	private String name;
	
	public boolean isEmpty() {
		return languageService.count() == 0;
	}
	

	
	@Transactional
	public void populate(String contextName) throws ServiceException {
		this.name =  contextName;
		
		createSecurityGroups();
		createLanguages();
		createCountries();
		createZones();
		createCurrencies();
		createSubReferences();
		createModules();
		createMerchant();


	}
	
	private void createSecurityGroups() throws ServiceException {
		
		  //create permissions
		  //Map name object
		  Map<String, Permission> permissionKeys = new HashMap<String, Permission>();
		  Permission AUTH = new Permission("AUTH");
		  permissionService.create(AUTH);
		  permissionKeys.put(AUTH.getPermissionName(), AUTH);
		  
		  Permission SUPERADMIN = new Permission("SUPERADMIN");
		  permissionService.create(SUPERADMIN);
		  permissionKeys.put(SUPERADMIN.getPermissionName(), SUPERADMIN);
		  
		  Permission ADMIN = new Permission("ADMIN");
		  permissionService.create(ADMIN);
		  permissionKeys.put(ADMIN.getPermissionName(), ADMIN);
		  
		  Permission PRODUCTS = new Permission("PRODUCTS");
		  permissionService.create(PRODUCTS);
		  permissionKeys.put(PRODUCTS.getPermissionName(), PRODUCTS);
		  
		  Permission ORDER = new Permission("ORDER");
		  permissionService.create(ORDER);
		  permissionKeys.put(ORDER.getPermissionName(), ORDER);
		  
		  Permission CONTENT = new Permission("CONTENT");
		  permissionService.create(CONTENT);
		  permissionKeys.put(CONTENT.getPermissionName(), CONTENT);
		  
		  Permission STORE = new Permission("STORE");
		  permissionService.create(STORE);
		  permissionKeys.put(STORE.getPermissionName(), STORE);
		  
		  Permission TAX = new Permission("TAX");
		  permissionService.create(TAX);
		  permissionKeys.put(TAX.getPermissionName(), TAX);
		  
		  Permission PAYMENT = new Permission("PAYMENT");
		  permissionService.create(PAYMENT);
		  permissionKeys.put(PAYMENT.getPermissionName(), PAYMENT);
		  
		  Permission CUSTOMER = new Permission("CUSTOMER");
		  permissionService.create(CUSTOMER);
		  permissionKeys.put(CUSTOMER.getPermissionName(), CUSTOMER);
		  
		  Permission SHIPPING = new Permission("SHIPPING");
		  permissionService.create(SHIPPING);
		  permissionKeys.put(SHIPPING.getPermissionName(), SHIPPING);
		  
		  Permission AUTH_CUSTOMER = new Permission("AUTH_CUSTOMER");
		  permissionService.create(AUTH_CUSTOMER);
		  permissionKeys.put(AUTH_CUSTOMER.getPermissionName(), AUTH_CUSTOMER);
		
		  SecurityGroupsBuilder groupBuilder = new SecurityGroupsBuilder();
		  groupBuilder
		  .addGroup("SUPERADMIN", GroupType.ADMIN)
		  .addPermission(permissionKeys.get("AUTH"))
		  .addPermission(permissionKeys.get("SUPERADMIN"))
		  .addPermission(permissionKeys.get("ADMIN"))
		  .addPermission(permissionKeys.get("PRODUCTS"))
		  .addPermission(permissionKeys.get("ORDER"))
		  .addPermission(permissionKeys.get("CONTENT"))
		  .addPermission(permissionKeys.get("STORE"))
		  .addPermission(permissionKeys.get("TAX"))
		  .addPermission(permissionKeys.get("PAYMENT"))
		  .addPermission(permissionKeys.get("CUSTOMER"))
		  .addPermission(permissionKeys.get("SHIPPING"))
		  
		  .addGroup("ADMIN", GroupType.ADMIN)
		  .addPermission(permissionKeys.get("AUTH"))
		  .addPermission(permissionKeys.get("ADMIN"))
		  .addPermission(permissionKeys.get("PRODUCTS"))
		  .addPermission(permissionKeys.get("ORDER"))
		  .addPermission(permissionKeys.get("CONTENT"))
		  .addPermission(permissionKeys.get("STORE"))
		  .addPermission(permissionKeys.get("TAX"))
		  .addPermission(permissionKeys.get("PAYMENT"))
		  .addPermission(permissionKeys.get("CUSTOMER"))
		  .addPermission(permissionKeys.get("SHIPPING"))
		  
		  .addGroup("ADMIN_RETAILER", GroupType.ADMIN)
		  .addPermission(permissionKeys.get("AUTH"))
		  .addPermission(permissionKeys.get("ADMIN"))
		  .addPermission(permissionKeys.get("PRODUCTS"))
		  .addPermission(permissionKeys.get("ORDER"))
		  .addPermission(permissionKeys.get("CONTENT"))
		  .addPermission(permissionKeys.get("STORE"))
		  .addPermission(permissionKeys.get("TAX"))
		  .addPermission(permissionKeys.get("PAYMENT"))
		  .addPermission(permissionKeys.get("CUSTOMER"))
		  .addPermission(permissionKeys.get("SHIPPING"))
		  
		  .addGroup("ADMIN_STORE", GroupType.ADMIN)
		  .addPermission(permissionKeys.get("AUTH"))
		  .addPermission(permissionKeys.get("CONTENT"))
		  .addPermission(permissionKeys.get("STORE"))
		  .addPermission(permissionKeys.get("TAX"))
		  .addPermission(permissionKeys.get("PAYMENT"))
		  .addPermission(permissionKeys.get("CUSTOMER"))
		  .addPermission(permissionKeys.get("SHIPPING"))
		  
		  .addGroup("ADMIN_CATALOGUE", GroupType.ADMIN)
		  .addPermission(permissionKeys.get("AUTH"))
		  .addPermission(permissionKeys.get("PRODUCTS"))
		  
		  .addGroup("ADMIN_ORDER", GroupType.ADMIN)
		  .addPermission(permissionKeys.get("AUTH"))
		  .addPermission(permissionKeys.get("ORDER"))
		  
		  .addGroup("ADMIN_CONTENT", GroupType.ADMIN)
		  .addPermission(permissionKeys.get("AUTH"))
		  .addPermission(permissionKeys.get("CONTENT"))
		  
		  .addGroup("CUSTOMER", GroupType.CUSTOMER)
		  .addPermission(permissionKeys.get("AUTH"))
		  .addPermission(permissionKeys.get("AUTH_CUSTOMER"));
		  
		  for(Group g : groupBuilder.build()) {
			  groupService.create(g);
		  }

		
	}
	


	private void createCurrencies() throws ServiceException {
		LOGGER.info(String.format("%s : Populating Currencies ", name));

		for (String code : SchemaConstant.CURRENCY_MAP.keySet()) {
  
            try {
            	java.util.Currency c = java.util.Currency.getInstance(code);
            	
            	if(c==null) {
            		LOGGER.info(String.format("%s : Populating Currencies : no currency for code : %s", name, code));
            	}
            	
            		//check if it exist
            		
	            	Currency currency = new Currency();
	            	currency.setName(c.getCurrencyCode());
	            	currency.setCurrency(c);
	            	currencyService.create(currency);

            //System.out.println(l.getCountry() + "   " + c.getSymbol() + "  " + c.getSymbol(l));
            } catch (IllegalArgumentException e) {
            	LOGGER.info(String.format("%s : Populating Currencies : no currency for code : %s", name, code));
            }
        }  
	}

	private void createCountries() throws ServiceException {
		LOGGER.info(String.format("%s : Populating Countries ", name));
		List<Language> languages = languageService.list();
		for(String code : SchemaConstant.COUNTRY_ISO_CODE) {
			Locale locale = SchemaConstant.LOCALES.get(code);
			if (locale != null) {
				Country country = new Country(code);
				countryService.create(country);
				
				for (Language language : languages) {
					String name = locale.getDisplayCountry(new Locale(language.getCode()));
					//byte[] ptext = value.getBytes(Constants.ISO_8859_1); 
					//String name = new String(ptext, Constants.UTF_8); 
					CountryDescription description = new CountryDescription(language, name);
					countryService.addCountryDescription(country, description);
				}
			}
		}
	}
	
	private void createZones() throws ServiceException {
		LOGGER.info(String.format("%s : Populating Zones ", name));
        try {

    		  Map<String,Zone> zonesMap = new HashMap<String,Zone>();
    		  zonesMap = zonesLoader.loadZones("reference/zoneconfig.json");
    		  
    		  this.addZonesToDb(zonesMap);
/*              
              for (Map.Entry<String, Zone> entry : zonesMap.entrySet()) {
            	    String key = entry.getKey();
            	    Zone value = entry.getValue();
            	    if(value.getDescriptions()==null) {
            	    	LOGGER.warn("This zone " + key + " has no descriptions");
            	    	continue;
            	    }
            	    
            	    List<ZoneDescription> zoneDescriptions = value.getDescriptions();
            	    value.setDescriptons(null);

            	    zoneService.create(value);
            	    
            	    for(ZoneDescription description : zoneDescriptions) {
            	    	description.setZone(value);
            	    	zoneService.addDescription(value, description);
            	    }
              }*/
              
              //lookup additional zones
              //iterate configured languages
      		  LOGGER.info("Populating additional zones");

              //load reference/zones/* (zone config for additional country)
              //example in.json and in-fr.son
              //will load es zones and use a specific file for french es zones
      		  List<Map<String, Zone>> loadIndividualZones = zonesLoader.loadIndividualZones();
      		  
      		loadIndividualZones.forEach(this::addZonesToDb);

  		} catch (Exception e) {
  		    
  			throw new ServiceException(e);
  		}

	}

	
	private void addZonesToDb(Map<String,Zone> zonesMap) throws RuntimeException {
		
		try {
		
	        for (Map.Entry<String, Zone> entry : zonesMap.entrySet()) {
	    	    String key = entry.getKey();
	    	    Zone value = entry.getValue();

	    	    if(value.getDescriptions()==null) {
	    	    	LOGGER.warn("This zone " + key + " has no descriptions");
	    	    	continue;
	    	    }
	    	    
	    	    List<ZoneDescription> zoneDescriptions = value.getDescriptions();
	    	    value.setDescriptons(null);
	
	    	    zoneService.create(value);
	    	    
	    	    for(ZoneDescription description : zoneDescriptions) {
	    	    	description.setZone(value);
	    	    	zoneService.addDescription(value, description);
	    	    }
	        }
        
		}catch(Exception e) {
			LOGGER.error("An error occured while loading zones",e);
			
		}
		
	}
	
	private void createLanguages() throws ServiceException {
		LOGGER.info(String.format("%s : Populating Languages ", name));
		for(String code : SchemaConstant.LANGUAGE_ISO_CODE) {
			Language language = new Language(code);
			languageService.create(language);
		}
	}
	
	private void createMerchant() throws ServiceException {
		LOGGER.info(String.format("%s : Creating merchant ", name));
		
		Date date = new Date(System.currentTimeMillis());
		
		Language en = languageService.getByCode("en");
		Language fr = languageService.getByCode("fr");
		Country cm = countryService.getByCode("CM");
		Currency currency = currencyService.getByCode("XAF");
		Zone qc = zoneService.getByCode("QC");
		
		List<Language> supportedLanguages = new ArrayList<Language>();
		supportedLanguages.add(en);
		supportedLanguages.add(fr);
		
		//create a merchant
		MerchantStore store = new MerchantStore();
		store.setCountry(cm);
		store.setCurrency(currency);
		store.setDefaultLanguage(fr);
		store.setInBusinessSince(date);
		//store.setZone(qc);
		store.setStorename("Default store");
		store.setStorephone("888-888-8888");
		store.setCode(MerchantStore.DEFAULT_STORE);
		store.setStorecity("My city");
		store.setPriceDollars(new BigDecimal(510));
		store.setPercentageProfitRate(new BigDecimal(10));
		store.setStorestateprovince("My State Province");
		store.setStoreaddress("1234 Street address");
		store.setStorepostalcode("H2H-2H2");
		store.setStoreEmailAddress("john@test.com");
		store.setDomainName("http://localhost:8080");
		store.setStoreTemplate("december");
		store.setRetailer(true);
		store.setLanguages(supportedLanguages);
		
		merchantService.create(store);
		
		
		TaxClass taxclass = new TaxClass(TaxClass.DEFAULT_TAX_CLASS);
		taxclass.setMerchantStore(store);
		
		taxClassService.create(taxclass);
		
		//create default manufacturer
		Manufacturer defaultManufacturer = new Manufacturer();
		defaultManufacturer.setCode("DEFAULT");
		defaultManufacturer.setMerchantStore(store);
		
		ManufacturerDescription manufacturerDescriptionEn = new ManufacturerDescription();
		manufacturerDescriptionEn.setLanguage(en);
		manufacturerDescriptionEn.setName("DEFAULT");
		manufacturerDescriptionEn.setManufacturer(defaultManufacturer);
		manufacturerDescriptionEn.setDescription("DEFAULT");
		
		ManufacturerDescription manufacturerDescriptionFr = new ManufacturerDescription();
		manufacturerDescriptionFr.setLanguage(fr);
		manufacturerDescriptionFr.setName("DEFAUT");
		manufacturerDescriptionFr.setManufacturer(defaultManufacturer);
		manufacturerDescriptionFr.setDescription("DEFAUT");
		
		
		defaultManufacturer.getDescriptions().add(manufacturerDescriptionEn);
		defaultManufacturer.getDescriptions().add(manufacturerDescriptionFr);
		
		manufacturerService.create(defaultManufacturer);		
		
	   Optin newsletter = new Optin();
	   newsletter.setCode(OptinType.NEWSLETTER.name());
	   newsletter.setMerchant(store);
	   newsletter.setOptinType(OptinType.NEWSLETTER);
	   optinService.create(newsletter);	   
		
		createCategory(store, en, fr);		
		
		
	}
	/**
	 * 
	 * @param store
	 * @param en
	 * @param fr
	 * @throws ServiceException
	 */
	private void createCategory(MerchantStore store, Language en, Language fr) throws ServiceException {
		LOGGER.info(String.format("%s : Creating category ", name));		
		
		//create default Category FEMALE
		Category categoryF = new Category();
		CategoryDescription categoryDescriptionFEn = new CategoryDescription();		
		CategoryDescription categoryDescriptionFrF = new CategoryDescription();	
		categoryF.getDescriptions().add(categoryDescriptionFEn);
		categoryF.getDescriptions().add(categoryDescriptionFrF);
		categoryF.setMerchantStore(store);
		categoryF.setParent(null);
		categoryF.setCode("KF01");
		categoryF.setVisible(true);
		categoryF.setFeatured(false);
		categoryF.setLineage("/" + categoryF.getId() + "/");
		categoryF.setDepth(0);
		categoryF.setSortOrder(0);			
		categoryDescriptionFEn.setCategory(categoryF);
		categoryDescriptionFEn.setLanguage(en);
		categoryDescriptionFEn.setName("FAMALE");
		categoryDescriptionFEn.setSeUrl("female");
		categoryDescriptionFrF.setCategory(categoryF);
		categoryDescriptionFrF.setLanguage(fr);
		categoryDescriptionFrF.setName("FEMME");
		categoryDescriptionFrF.setSeUrl("femme");				
		categoryService.create(categoryF);
		
		//create default Category MALE
		Category categoryH = new Category();
		CategoryDescription categoryDescriptionHEn = new CategoryDescription();		
		CategoryDescription categoryDescriptionHFr = new CategoryDescription();	
		categoryH.getDescriptions().add(categoryDescriptionHEn);
		categoryH.getDescriptions().add(categoryDescriptionHFr);
		categoryH.setMerchantStore(store);
		categoryH.setParent(null);
		categoryH.setCode("KH01");
		categoryH.setVisible(true);
		categoryH.setFeatured(false);
		categoryH.setSortOrder(0);	
		categoryH.setLineage("/" + categoryH.getId() + "/");
		categoryH.setDepth(0);		
		categoryDescriptionHEn.setCategory(categoryH);
		categoryDescriptionHEn.setLanguage(en);
		categoryDescriptionHEn.setName("MALE");
		categoryDescriptionHEn.setSeUrl("male");
		categoryDescriptionHFr.setCategory(categoryH);
		categoryDescriptionHFr.setLanguage(fr);
		categoryDescriptionHFr.setName("HOMME");
		categoryDescriptionHFr.setSeUrl("homme");				
		categoryService.create(categoryH);
		
		//create default Category CHILD
		Category categoryE = new Category();
		CategoryDescription categoryDescriptionEEn = new CategoryDescription();		
		CategoryDescription categoryDescriptionEFr = new CategoryDescription();	
		categoryE.getDescriptions().add(categoryDescriptionEEn);
		categoryE.getDescriptions().add(categoryDescriptionEFr);
		categoryE.setMerchantStore(store);
		categoryE.setParent(null);
		categoryE.setCode("KE01");
		categoryE.setVisible(true);
		categoryE.setFeatured(false);
		categoryE.setSortOrder(0);			
		categoryE.setLineage("/" + categoryE.getId() + "/");
		categoryE.setDepth(0);			
		categoryDescriptionEEn.setCategory(categoryE);
		categoryDescriptionEEn.setLanguage(en);
		categoryDescriptionEEn.setName("CHILD");
		categoryDescriptionEEn.setSeUrl("child");
		categoryDescriptionEFr.setCategory(categoryE);
		categoryDescriptionEFr.setLanguage(fr);
		categoryDescriptionEFr.setName("ENFANT");
		categoryDescriptionEFr.setSeUrl("enfant");				
		categoryService.create(categoryE);
		
		//create sub Category FEMALE clothing		
		Category subCategoryV = new Category();
		CategoryDescription categoryDescriptionVEn = new CategoryDescription();		
		CategoryDescription categoryDescriptionVFr = new CategoryDescription();	
		subCategoryV.getDescriptions().add(categoryDescriptionVEn);
		subCategoryV.getDescriptions().add(categoryDescriptionVFr);
		subCategoryV.setMerchantStore(store);
		subCategoryV.setParent(categoryF);
		subCategoryV.setCode("237VTF");
		subCategoryV.setVisible(true);
		subCategoryV.setFeatured(false);
		subCategoryV.setSortOrder(0);		
		categoryDescriptionVEn.setCategory(subCategoryV);
		categoryDescriptionVEn.setLanguage(en);
		categoryDescriptionVEn.setName("clothing");
		categoryDescriptionVEn.setSeUrl("clothing_Woman");
		categoryDescriptionVFr.setCategory(subCategoryV);
		categoryDescriptionVFr.setLanguage(fr);
		categoryDescriptionVFr.setName("VETEMENTS");
		categoryDescriptionVFr.setSeUrl("vêtements_femmes");
		categoryService.create(subCategoryV);
		Category parentV = new Category();
        parentV.setId(categoryF.getId());
        parentV.setMerchantStore(store);
        categoryService.addChild(parentV, subCategoryV);
		
		//create sub Category FEMALE Bags		
		Category subCategoryL = new Category();
		CategoryDescription categoryDescriptionLEn = new CategoryDescription();		
		CategoryDescription categoryDescriptionLFr = new CategoryDescription();	
		subCategoryL.getDescriptions().add(categoryDescriptionLEn);
		subCategoryL.getDescriptions().add(categoryDescriptionLFr);
		subCategoryL.setMerchantStore(store);
		subCategoryL.setParent(categoryF);
		subCategoryL.setCode("237BGF");
		subCategoryL.setVisible(true);
		subCategoryL.setFeatured(false);
		subCategoryL.setSortOrder(0);		
		categoryDescriptionLEn.setCategory(subCategoryL);
		categoryDescriptionLEn.setLanguage(en);
		categoryDescriptionLEn.setName("BAGS");
		categoryDescriptionLEn.setSeUrl("bags_woman");
		categoryDescriptionLFr.setCategory(subCategoryL);
		categoryDescriptionLFr.setLanguage(fr);
		categoryDescriptionLFr.setName("SACS");
		categoryDescriptionLFr.setSeUrl("sacs_femmes");
		categoryService.create(subCategoryL);
		Category parentL = new Category();
        parentL.setId(categoryF.getId());
        parentL.setMerchantStore(store);
        categoryService.addChild(parentL, subCategoryL);
		
		//create sub Category FEMALE shoes		
		Category subCategoryC = new Category();
		CategoryDescription categoryDescriptionCEn = new CategoryDescription();		
		CategoryDescription categoryDescriptionCFr = new CategoryDescription();	
		subCategoryC.getDescriptions().add(categoryDescriptionCEn);
		subCategoryC.getDescriptions().add(categoryDescriptionCFr);
		subCategoryC.setMerchantStore(store);
		subCategoryC.setParent(categoryF);
		subCategoryC.setCode("237CHF");
		subCategoryC.setVisible(true);
		subCategoryC.setFeatured(false);
		subCategoryC.setSortOrder(0);		
		categoryDescriptionCEn.setCategory(subCategoryC);
		categoryDescriptionCEn.setLanguage(en);
		categoryDescriptionCEn.setName("SHOES");
		categoryDescriptionCEn.setSeUrl("shoes_woman");
		categoryDescriptionCFr.setCategory(subCategoryC);
		categoryDescriptionCFr.setLanguage(fr);
		categoryDescriptionCFr.setName("CHAUSSURES");
		categoryDescriptionCFr.setSeUrl("chaussures_femmes");
		categoryService.create(subCategoryC);
		Category parentC = new Category();
        parentC.setId(categoryF.getId());
        parentC.setMerchantStore(store);
        categoryService.addChild(parentC, subCategoryC);
		
		
			
		//create sub Category MALE clothing		
		Category subCategoryV1 = new Category();
		CategoryDescription categoryDescriptionV1En = new CategoryDescription();		
		CategoryDescription categoryDescriptionV1Fr = new CategoryDescription();	
		subCategoryV1.getDescriptions().add(categoryDescriptionV1En);
		subCategoryV1.getDescriptions().add(categoryDescriptionV1Fr);
		subCategoryV1.setMerchantStore(store);
		subCategoryV1.setParent(categoryH);
		subCategoryV1.setCode("237VTM");
		subCategoryV1.setVisible(true);
		subCategoryV1.setFeatured(false);
		subCategoryV1.setSortOrder(0);		
		categoryDescriptionV1En.setCategory(subCategoryV1);
		categoryDescriptionV1En.setLanguage(en);
		categoryDescriptionV1En.setName("clothing");
		categoryDescriptionV1En.setSeUrl("clothing");
		categoryDescriptionV1Fr.setCategory(subCategoryV1);
		categoryDescriptionV1Fr.setLanguage(fr);
		categoryDescriptionV1Fr.setName("VETEMENTS");
		categoryDescriptionV1Fr.setSeUrl("vêtements");
		categoryService.create(subCategoryV1);
		Category parentV1 = new Category();
		parentV1.setId(categoryH.getId());
        parentV1.setMerchantStore(store);
        categoryService.addChild(parentV1, subCategoryV1);
		
		//create sub Category Male bags		
		Category subCategoryB = new Category();
		CategoryDescription categoryDescriptionBEn = new CategoryDescription();		
		CategoryDescription categoryDescriptionBFr = new CategoryDescription();	
		subCategoryB.getDescriptions().add(categoryDescriptionBEn);
		subCategoryB.getDescriptions().add(categoryDescriptionBFr);
		subCategoryB.setMerchantStore(store);
		subCategoryB.setParent(categoryH);
		subCategoryB.setCode("237BGH");
		subCategoryB.setVisible(true);
		subCategoryB.setFeatured(false);
		subCategoryB.setSortOrder(0);		
		categoryDescriptionBEn.setCategory(subCategoryB);
		categoryDescriptionBEn.setLanguage(en);
		categoryDescriptionBEn.setName("BAGS");
		categoryDescriptionBEn.setSeUrl("bags");
		categoryDescriptionBFr.setCategory(subCategoryB);
		categoryDescriptionBFr.setLanguage(fr);
		categoryDescriptionBFr.setName("SACS");
		categoryDescriptionBFr.setSeUrl("sacs");
		categoryService.create(subCategoryB);
		Category parentB = new Category();
        parentB.setId(categoryH.getId());
        parentB.setMerchantStore(store);
        categoryService.addChild(parentB, subCategoryB);
		
		//create sub Category MALE shoes		
		Category subCategoryC1 = new Category();
		CategoryDescription categoryDescriptionC1En = new CategoryDescription();		
		CategoryDescription categoryDescriptionC1Fr = new CategoryDescription();	
		subCategoryC1.getDescriptions().add(categoryDescriptionC1En);
		subCategoryC1.getDescriptions().add(categoryDescriptionC1Fr);
		subCategoryC1.setMerchantStore(store);
		subCategoryC1.setParent(categoryH);
		subCategoryC1.setCode("237CHH");
		subCategoryC1.setVisible(true);
		subCategoryC1.setFeatured(false);
		subCategoryC1.setSortOrder(0);		
		categoryDescriptionC1En.setCategory(subCategoryC1);
		categoryDescriptionC1En.setLanguage(en);
		categoryDescriptionC1En.setName("SHOES");
		categoryDescriptionC1En.setSeUrl("shoes");
		categoryDescriptionC1Fr.setCategory(subCategoryC1);
		categoryDescriptionC1Fr.setLanguage(fr);
		categoryDescriptionC1Fr.setName("CHAUSSURES");
		categoryDescriptionC1Fr.setSeUrl("chaussures");
		categoryService.create(subCategoryC1);
		Category parentC1 = new Category();
        parentC1.setId(categoryH.getId());
        parentC1.setMerchantStore(store);
        categoryService.addChild(parentC1, subCategoryC1);
		
		//create sub Category baby	
		Category subCategoryCH = new Category();
		CategoryDescription categoryDescriptionCHEn = new CategoryDescription();		
		CategoryDescription categoryDescriptionCHFr = new CategoryDescription();	
		subCategoryCH.getDescriptions().add(categoryDescriptionCHEn);
		subCategoryCH.getDescriptions().add(categoryDescriptionCHFr);
		subCategoryCH.setMerchantStore(store);
		subCategoryCH.setParent(categoryE);
		subCategoryCH.setCode("237VTBA");
		subCategoryCH.setVisible(true);
		subCategoryCH.setFeatured(false);
		subCategoryCH.setSortOrder(0);		
		categoryDescriptionCHEn.setCategory(subCategoryCH);
		categoryDescriptionCHEn.setLanguage(en);
		categoryDescriptionCHEn.setName("BABY");
		categoryDescriptionCHEn.setSeUrl("baby");
		categoryDescriptionCHFr.setCategory(subCategoryCH);
		categoryDescriptionCHFr.setLanguage(fr);
		categoryDescriptionCHFr.setName("BEBE");
		categoryDescriptionCHFr.setSeUrl("bebe");
		categoryService.create(subCategoryCH);
		Category parentCH = new Category();
        parentCH.setId(categoryE.getId());
        parentCH.setMerchantStore(store);
        categoryService.addChild(parentCH, subCategoryCH);
		
		//create sub Category Girl		
		Category subCategoryGl = new Category();
		CategoryDescription categoryDescriptionG1En = new CategoryDescription();		
		CategoryDescription categoryDescriptionG1Fr = new CategoryDescription();	
		subCategoryGl.getDescriptions().add(categoryDescriptionG1En);
		subCategoryGl.getDescriptions().add(categoryDescriptionG1Fr);
		subCategoryGl.setMerchantStore(store);
		subCategoryGl.setParent(categoryE);
		subCategoryGl.setCode("237BGBA");
		subCategoryGl.setVisible(true);
		subCategoryGl.setFeatured(false);
		subCategoryGl.setSortOrder(0);		
		categoryDescriptionG1En.setCategory(subCategoryGl);
		categoryDescriptionG1En.setLanguage(en);
		categoryDescriptionG1En.setName("GIRLS");
		categoryDescriptionG1En.setSeUrl("girls");
		categoryDescriptionG1Fr.setCategory(subCategoryGl);
		categoryDescriptionG1Fr.setLanguage(fr);
		categoryDescriptionG1Fr.setName("FILLES");
		categoryDescriptionG1Fr.setSeUrl("filles");
		categoryService.create(subCategoryGl);
		Category parentG1 = new Category();
        parentG1.setId(categoryE.getId());
        parentG1.setMerchantStore(store);
        categoryService.addChild(parentG1, subCategoryGl);
		
		//create sub Category BOYS		
		Category subCategoryBy = new Category();
		CategoryDescription categoryDescriptionByEn = new CategoryDescription();		
		CategoryDescription categoryDescriptionByFr = new CategoryDescription();	
		subCategoryBy.getDescriptions().add(categoryDescriptionByEn);
		subCategoryBy.getDescriptions().add(categoryDescriptionByFr);
		subCategoryBy.setMerchantStore(store);
		subCategoryBy.setParent(categoryE);
		subCategoryBy.setCode("237CHBA");
		subCategoryBy.setVisible(true);
		subCategoryBy.setFeatured(false);
		subCategoryBy.setSortOrder(0);		
		categoryDescriptionByEn.setCategory(subCategoryBy);
		categoryDescriptionByEn.setLanguage(en);
		categoryDescriptionByEn.setName("BOYS");
		categoryDescriptionByEn.setSeUrl("boys");
		categoryDescriptionByFr.setCategory(subCategoryBy);
		categoryDescriptionByFr.setLanguage(fr);
		categoryDescriptionByFr.setName("GARCONS");
		categoryDescriptionByFr.setSeUrl("garcons");
		categoryService.create(subCategoryBy);
		Category parentBy = new Category();	
        parentBy.setId(categoryE.getId());
        parentBy.setMerchantStore(store);
        categoryService.addChild(parentBy, subCategoryBy);					
						
						
	}
	
	

	private void createModules() throws ServiceException {
		
		try {
			
			List<IntegrationModule> modules = modulesLoader.loadIntegrationModules("reference/integrationmodules.json");
            for (IntegrationModule entry : modules) {
        	    moduleConfigurationService.create(entry);
          }
			
			
		} catch (Exception e) {
			throw new ServiceException(e);
		}
		
		
	}
	
	private void createSubReferences() throws ServiceException {
		
		LOGGER.info(String.format("%s : Loading catalog sub references ", name));
		
		
		ProductType productType = new ProductType();
		productType.setCode(ProductType.GENERAL_TYPE);
		productTypeService.create(productType);


		
		
	}
	

	



}
