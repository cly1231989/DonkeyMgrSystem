/*
 * Copyright 2012-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hose.controller;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageOutputStream;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hose.model.Donkey;
import com.hose.model.DonkeyPageInfo;
import com.hose.model.DonkeyVersion;
import com.hose.model.Filters;
import com.hose.model.ImageUploadResult;
import com.hose.model.ImageUrl;
import com.hose.model.ImagesInfo;
import com.hose.model.User;
import com.hose.model.UserPageInfo;
import com.hose.service.DonkeyRepository;
import com.hose.service.DonkeyService;
import com.hose.service.UserService;
import com.hose.util.FilePathUtil;
import com.hose.util.ImageUtil;


@Controller
@SessionAttributes({"userid", "username"})
public class WebController {

	private String imagePath = "assets\\images\\gallery\\";
	private String needLoginString = "{\"result\":\"timeout\"}";
	private String successString = "{\"result\":\"success\"}";
	private String failedString = "{\"result\":\"error\"}";
	private String invalidParaString = "{\"result\":\"invalid paremeter\"}";
	private String oldPwdError = "{\"result\":\"old_pwd_error\"}";
	private FilePathUtil filePathUtil = new FilePathUtil();
	private static Map<Integer, Integer> versionInfo = new HashMap<Integer, Integer>();
	ObjectMapper mapper = new ObjectMapper(); 
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private DonkeyService donkeyService;
	
	@PostConstruct
    public void init(){
		versionInfo.clear();
		Iterable<Donkey> allDonkey = donkeyService.findAll();
		allDonkey.forEach( (donkey) -> versionInfo.put( donkey.getId(), donkey.getVersion()) );
    }
	
	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public DonkeyService getDonkeyService() {
		return donkeyService;
	}

	public void setDonkeyService(DonkeyService donkeyService) {
		this.donkeyService = donkeyService;
	}

	@RequestMapping("/")
	public String welcome(Map<String, Object> model) {
		Long userId= (Long) model.get("userid");
		if(userId == null)
			return "login";
		else
			return "forward:/index";
	}
	
	@RequestMapping("/index")
	public String index(Map<String, Object> model)
	{
		Long userId= (Long) model.get("userid");
		if(userId == null)
			return "login";
		
		User user = userService.getUser(userId);
		if(user.getTypeid() == 1){			//管理员
			return "manage_index";
		}else{
			return "index";
		}
	}
	
	@ResponseBody
	@RequestMapping(value="/user/pwd", method=RequestMethod.POST)
    public String modifyPwd( HttpServletRequest request, Map<String, Object> model ) {
		Long userId= (Long) model.get("userid");
		if(userId == null)
			return needLoginString;
		
		User user = userService.getUser(userId);
		
		String oldPwd = request.getParameter("oldpwd");
		if( oldPwd.compareTo(user.getPwd()) != 0 ){
			return oldPwdError;
		}
		
		String pwd = request.getParameter("pwd");
		user.setPwd(pwd);
		userService.saveUser(user);
		return successString;
	}
	
	@ResponseBody
	@RequestMapping(value="/login-rest", method=RequestMethod.POST)
    public String restLogin( @RequestParam String name, @RequestParam String pwd, HttpServletRequest request, Map<String, Object> model ) {
		request.getSession();
		Long userId= (Long) model.get("userid");
			
		User user = userService.getUser(name);
		if(user == null)
			return failedString;
		
		if( user.getPwd().compareTo(pwd) != 0 )
			return failedString;
		
		model.put("userid", user.getId());
		model.put( "username", user.getUsername() );
		return successString;
    }
	
	@ResponseBody
	@RequestMapping("/logout-rest")
	public String logoutRest(HttpServletRequest request, Map<String, Object> model)
	{
		request.getSession().invalidate();
		model.remove("userid");
		return successString;
	}
	
	@RequestMapping("/logout")
	public String logout(HttpServletRequest request, Map<String, Object> model)
	{
		request.getSession().invalidate();
		model.remove("userid");
		return "login";
	}
	
	@ResponseBody
	@RequestMapping(value="/user/page", method=RequestMethod.POST)
    public UserPageInfo userPage( HttpServletRequest request, Map<String, Object> model ) {
		int pageth = Integer.parseInt( request.getParameter("page") );
		int rows = Integer.parseInt( request.getParameter("rows") );
		String sidx = request.getParameter("sidx");
		String sord = request.getParameter("sord");
		String search = request.getParameter("_search");
		String searchField = request.getParameter("searchField");
		String searchString = request.getParameter("searchString");
		
		long totalCount = userService.getTotalCount();
		long tatalPage = totalCount/rows;
		if(totalCount%rows != 0){
			tatalPage++;
		}
		
		if(sidx.length() == 0)
			sidx = new String("id");

		Page<User> users;
		if(search.equalsIgnoreCase("true")){
			users = userService.getOneGroupUsersByCondition(searchField, searchString, rows*(pageth-1), rows, sidx, sord);
		}else{
			users = userService.getOneGroupUsersByCondition("", "", rows*(pageth-1), rows, sidx, sord);
		}
		
//		String filters = request.getParameter("filters");
//		Map<String, List<Map<String, Object>>> searchOptions = null;
//		
//		if(search.equalsIgnoreCase("true")){
//			try {
//				searchOptions = objectMapper.readValue(filters, Map.class);
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//		
//		long totalCount = userService.getTotalCount();
//		long tatalPage = totalCount/rows;
//		if(totalCount%rows != 0){
//			tatalPage++;
//		}
//		
//		if(sidx.length() == 0)
//			sidx = new String("id");
//
//		Page<User> users;
//		if(searchOptions == null || searchOptions.size() == 0){
//			users = userService.getOneGroupUsers(rows*(pageth-1), rows, sidx, sord);
//		}else{
//			List<Map<String, Object>> value = null;
//			Iterator<String> iter = searchOptions.keySet().iterator();
//			
//	        while (iter.hasNext()) {
//	            String field = iter.next();
//	            if(field.equalsIgnoreCase("rules")){
//	            	value = searchOptions.get(field);
//	            	break;
//	            }
//	        }
//	        
//	        if(value == null){
//	        	users = userService.getOneGroupUsers(rows*(pageth-1), rows, sidx, sord);
//	        }
//	        else{
//	        	if( ((String)value.get(0).get("field")).equalsIgnoreCase("name")){
//	        		users = userService.getOneGroupUsersByCondition("name", (String)value.get(0).get("data"), rows*(pageth-1), rows, sidx, sord);
//	        	}
//	        	else{
//	        		users = userService.getOneGroupUsersByCondition("username", (String)value.get(0).get("data"), rows*(pageth-1), rows, sidx, sord);
//	        	}
//	        }
//		}
		
		UserPageInfo usersInfo = new UserPageInfo(tatalPage, pageth, totalCount, 0, users.getContent());
		
		return usersInfo;
    }
	
	@ResponseBody
	@RequestMapping(value="/user/save", method=RequestMethod.POST)
    public String saveUser( HttpServletRequest request, Map<String, Object> model ) {
		String oper = request.getParameter("oper");
		String idStr = request.getParameter("id");
		
		if(oper.equalsIgnoreCase("del")){
			String str[] = idStr.split(",");  
			  
			for(int i = 0; i < str.length; i++){  
				userService.delUser( Long.parseLong(str[i]) );
			}

			return successString;
		}
		
		Long id = (long) 0;
		if(!idStr.equalsIgnoreCase("_empty")){
			id = Long.parseLong( idStr );
		}
		
		String name = request.getParameter("name");
		String pwd = request.getParameter("pwd");
		String username = request.getParameter("username");
		String phone = request.getParameter("phone");
		
		User user = null;
		
		if(id == 0){
			user = new User(id, name, pwd, username, phone);
		}else{
			user = userService.getUser(id);			
			user.setPwd(pwd);
			user.setUsername(username);
			user.setPhone(phone);
		}
		
		userService.saveUser(user);
						
		return successString;
    }	
	
	@ResponseBody
	@RequestMapping(value="/user/uservalidate", method=RequestMethod.POST)
    public String validateUserName( @RequestParam String name, Map<String, Object> model ) {
		if( userService.isUserNameExist(name) )
			return "false";
		
		return "true";
	}
	
	@ResponseBody
	@RequestMapping(value="/donkey/page", method=RequestMethod.POST)
    public DonkeyPageInfo donkeyPage( HttpServletRequest request, Map<String, Object> model ) {
		int pageth = Integer.parseInt( request.getParameter("page") );
		int rows = Integer.parseInt( request.getParameter("rows") );
		String sidx = request.getParameter("sidx");
		String sord = request.getParameter("sord");	
		String search = request.getParameter("_search");
//		String searchField = request.getParameter("searchField");
//		String searchString = request.getParameter("searchString");
		
		long totalCount = donkeyService.getTotalCount();
		long tatalPage = totalCount/rows;
		if(totalCount%rows != 0){
			tatalPage++;
		}
		
		if(sidx.length() == 0)
			sidx = new String("sn");

		Page<Donkey> donkeys;
		if(search.equalsIgnoreCase("true")){
			Filters filters = null;
			try {
				String searchField = request.getParameter("filters");
				filters = new ObjectMapper().readValue(searchField, Filters.class);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			donkeys = donkeyService.getOneGroupDonkeysByCondition(filters, pageth-1, rows, sidx, sord);
		}else{
			donkeys = donkeyService.getOneGroupDonkeys(pageth-1, rows, sidx, sord);
		}
				
		DonkeyPageInfo donkeysInfo = new DonkeyPageInfo(tatalPage, pageth, totalCount, 0, donkeys.getContent());
		return donkeysInfo;
    }
	
	@ResponseBody
	@RequestMapping(value="/donkey/save", method=RequestMethod.POST)
    public String saveDonkey( HttpServletRequest request, Map<String, Object> model ) {
		String oper = request.getParameter("oper");
		String idStr = request.getParameter("id");
		
		if(oper.equalsIgnoreCase("del")){
			String str[] = idStr.split(",");  
			  
			for(int i = 0; i < str.length; i++){  
				
				Integer donkeyID = Integer.parseInt(str[i]);
				Donkey donkey = donkeyService.getDonkey(donkeyID);	
				if(donkey == null)
					continue;
				
				String imageDir = donkey.getImagedir();
				String webRootPath = FilePathUtil.getWebRootPath();
				File fileMgr = new File(webRootPath + imageDir);
				File[] files = fileMgr.listFiles();
				if(files != null && files.length != 0){
					for(File file:files){
						file.delete();
					}
				}
				
				versionInfo.remove( donkey.getId() );
			    donkeyService.delDonkey( donkeyID );
			}

			return successString;
		}
		
		Integer id = 0;
		if(oper.equalsIgnoreCase("edit")){
			id = Integer.parseInt( idStr );
		}
		
		Integer sn = Integer.parseInt( request.getParameter("sn") );
		Integer version = Integer.parseInt( request.getParameter("version") );
		String farmer = request.getParameter("farmer");
		String breedaddress = request.getParameter("breedaddress");
		String supplier = request.getParameter("supplier");
		String supplyaddress = request.getParameter("supplyaddress");
		String dealtime = request.getParameter("dealtime");
		String supplytime = request.getParameter("supplytime");
		Integer breed = Integer.parseInt( request.getParameter("breed") );
		Integer sex = Integer.parseInt( request.getParameter("sex") );
		//String agewhendeal = request.getParameter("agewhendeal");
		String agewhenkill = request.getParameter("agewhenkill");
		//String feedstatus = request.getParameter("feedstatus");
		String feedpattern = request.getParameter("feedpattern");
		String forage = request.getParameter("forage");
		String healthstatus = request.getParameter("healthstatus");
		String breedstatus = request.getParameter("breedstatus");
		String killdepartment = request.getParameter("killdepartment");
		//String killplace = request.getParameter("killplace");
		String killtime = request.getParameter("killtime");
		//String splitstatus = request.getParameter("splitstatus");
		//String processstatus = request.getParameter("processstatus");
		String qualitystatus = request.getParameter("qualitystatus");
		String freshkeepmethod = request.getParameter("freshkeepmethod");
		String freshkeeptime = request.getParameter("freshkeeptime");
		String QC = request.getParameter("qc");
		String QA = request.getParameter("qa");
		String furquality = request.getParameter("furquality");
		String reserved = request.getParameter("reserved");
		//String factorytime = request.getParameter("factorytime");
		
		Donkey donkey = null;
		
		if(id == 0){
			String imagedir = FilePathUtil.getImgPathWriteToDB(sn);
			donkey = new Donkey(sn,  farmer,  breedaddress, supplier, supplyaddress, dealtime, supplytime, breed, sex, "", agewhenkill, feedpattern, forage, "",  healthstatus,
					 breedstatus, killdepartment, "", killtime, freshkeepmethod, freshkeeptime, "",  "", qualitystatus, QC, QA, furquality, reserved, "", imagedir, version, false);
		}else{
			donkey = donkeyService.getDonkey(id);		
			donkey.setSn(sn);
			donkey.setVersion(version);
			donkey.setFarmer(farmer);
			donkey.setBreedaddress(breedaddress);
			donkey.setSupplier(supplier);
			donkey.setSupplyaddress(supplyaddress);
			donkey.setDealtime(dealtime);
			donkey.setSupplytime(supplytime);
			donkey.setBreed(breed);
			donkey.setSex(sex);
			//donkey.setAgewhendeal(agewhendeal);
			donkey.setAgewhenkill(agewhenkill);
			//donkey.setFeedstatus(feedstatus);
			donkey.setFeedpattern(feedpattern);
			donkey.setForage(forage);
			donkey.setHealthstatus(healthstatus);
			donkey.setBreedstatus(breedstatus);
			donkey.setKilldepartment(killdepartment);
			//donkey.setKillplace(killplace);
			donkey.setKilltime(killtime);
			//donkey.setSplitstatus(splitstatus);
			//donkey.setProcessstatus(processstatus);
			donkey.setQualitystatus(qualitystatus);
			donkey.setFreshkeepmethod(freshkeepmethod);
			donkey.setFreshkeeptime(freshkeeptime);
			donkey.setQc(QC);
			donkey.setQa(QA);
			donkey.setFurquality(furquality);
			donkey.setReserved(reserved);
			//donkey.setFactorytime(factorytime);
		}
		
		Donkey donkeyReturn = donkeyService.saveDonkey(donkey);
		versionInfo.put( donkeyReturn.getId(), donkeyReturn.getVersion() );
						
		//return successString;
		return "{\"result\":\"success\", \"id\":\""+donkeyReturn.getId()+"\"}";
    }	
	
	@ResponseBody
	@RequestMapping(value="/donkey/images", method=RequestMethod.POST)
    public ImagesInfo getDonkeyImages( HttpServletRequest request, Map<String, Object> model ) {
		String idStr = request.getParameter("id");
		if(idStr == null)
			return null;
		
		Integer id = Integer.parseInt( idStr );
		String webRootPath = FilePathUtil.getWebRootPath();
		
		ImagesInfo imagesInfo = new ImagesInfo();
		imagesInfo.count = 0;
		
		String imageDir = donkeyService.getDonkey(id).getImagedir();
		File fileMgr = new File(webRootPath + imageDir);
		File[] files = fileMgr.listFiles();
		if(files == null || files.length == 0)
			return imagesInfo;
		
		
		for(File file:files){
			ImageUrl imageUrl = new ImageUrl();
			String fileName = file.getAbsolutePath();
			if(fileName.indexOf("thumb") == -1)
				continue;
			
			fileName = fileName.substring(fileName.indexOf(webRootPath)+webRootPath.length()).replace("\\", "/");
			imageUrl.setUrl( fileName );
			imagesInfo.getImages().add(imageUrl);  
			imagesInfo.count++;
		}
		
		return imagesInfo;
	}
	
	@ResponseBody
	@RequestMapping(value="/donkey/images/delimage")
    public String delImages( HttpServletRequest request, Map<String, Object> model ) {
		String imgName = request.getParameter("img");
		String webRootPath = FilePathUtil.getWebRootPath();
		
		File thumbImg = new File(webRootPath + imgName);
		File imgFile = new File( webRootPath + imgName.replace("thumb", "image") );
		thumbImg.delete();
		imgFile.delete();
		//String imageDir = donkeyService.getDonkey(id).getImagedir();
		return successString;
	}
	
	@ResponseBody
	@RequestMapping(method = RequestMethod.POST, value = "/donkey/images/upload")
	public String imageUpload(MultipartHttpServletRequest request, Map<String, Object> model) {
	
		Long userId= (Long) model.get("userid");
		if(userId == null){
			return needLoginString;
		}
		
		Integer donkeyid = Integer.parseInt( request.getParameter("donkeyid") );
		Donkey donkey = donkeyService.getDonkey(donkeyid);
		MultipartFile image = (MultipartFile)request.getFile("image");
		if( image.isEmpty() )
			return invalidParaString;
		
		String fileName = FilePathUtil.getWebRootPath();
		String thumbName = FilePathUtil.getImgPathWriteToDB(donkey.getSn());
   
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH_mm_ss_SSS");
		String imageName = thumbName; 
		File f = new File(fileName + imageName);
		if (!f.exists()) {
			f.mkdirs();
		}
		
		thumbName += "\\thumb-" + df.format(new Date()) + ".jpg";
		imageName += "\\image-" + df.format(new Date()) + ".jpg";
		
		if( !ImageUtil.makeThumbImgage(image, fileName + imageName, fileName + thumbName) )
			return failedString;
		
		ImageUploadResult imageUploadResult = new ImageUploadResult("success", thumbName);
		try {
			return mapper.writeValueAsString(imageUploadResult);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return failedString;
		}			 
	}
	
	@ResponseBody
	@RequestMapping(method = RequestMethod.POST, value = "/donkey/snvalidate")
	public String validateSn(@RequestParam Integer sn, HttpServletRequest request, Map<String, Object> model) {
		String idStr = request.getParameter("id");
		Integer id = 0;
		if(idStr != null)
			id = Integer.parseInt(idStr);
		
		if( donkeyService.isSnExist(id, sn) )
			return "false";
		
		return "true";
	}
	
	@ResponseBody
	@RequestMapping(method = RequestMethod.POST, value = "/donkey/json/sn/{sn}")
	public Donkey getDonkeyBySn(@PathVariable Integer sn, Map<String, Object> model) {
		return donkeyService.getDonkeyBySn(sn);
	}
	
	@ResponseBody
	@RequestMapping(method = RequestMethod.POST, value = "/donkey/json/id/{id}")
	public Donkey getDonkeyById(@PathVariable Integer id, Map<String, Object> model) {
		return donkeyService.getDonkey(id);
	}
	
	@RequestMapping("/{sn}")
	public String getDonkeyForWeb(@PathVariable Integer sn, Map<String, Object> model) {
		Donkey donkey = donkeyService.getDonkeyBySn(sn);
		model.put("donkey", donkey);
		
		List<String> imageUrls = new ArrayList();
		String imageDir = donkey.getImagedir();
		String webRootPath = FilePathUtil.getWebRootPath();
		File fileMgr = new File(webRootPath + imageDir);
		File[] files = fileMgr.listFiles();
		if(files != null && files.length != 0){
			for(File file:files){
				if(file.getName().contains("thumb"))
					continue;
				
				String imageUrl = imageDir + "\\" + file.getName();
				imageUrls.add(imageUrl);
			}
		}
		
		model.put("imageurls", imageUrls);
		return "showdonkey";
	}
	
	@ResponseBody
	@RequestMapping(method = RequestMethod.POST, value = "/donkey/getdonkeys")
	public Map<Integer, Integer> getDonkeys(HttpServletRequest request) {
//		String versionStr = request.getParameter("versions");
//		List<Donkey> donkeysReturn = new ArrayList<Donkey>();
//		Map<Integer, Long> verinfo;
//		try {
//			verinfo = mapper.readValue(versionStr, Map.class);
//			
//			for ( Map.Entry<Integer, Long> entry : verinfo.entrySet() ) {  	
//				Long ver = versionInfo.get( entry.getKey() );
//				if(ver > entry.getValue())
//					donkeysReturn.add( donkeyService.getDonkeyBySn( entry.getKey() ) );  
//			}
//			
//			for( Map.Entry<Integer, Long> entry : versionInfo.entrySet() ){
//				Long ver = verinfo.get( entry.getKey() );
//				if(ver == null)
//					donkeysReturn.add( donkeyService.getDonkeyBySn( entry.getKey() ) );
//			}
//		} catch (JsonParseException e) {
//			e.printStackTrace();
//		} catch (JsonMappingException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		} 
		
		return versionInfo;
	}
}
