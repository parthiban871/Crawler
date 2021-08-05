package com.example.crawler.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.naming.directory.SearchResult;

import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.crawler.entity.FoundUrl;

@Controller
public class CrawlerController {
	private static final int MAX_PAGES_TO_SEARCH = 60;
    private Set<String> pagesVisited = new HashSet<String>();
    private List<String> pagesToVisit = new LinkedList<String>();
    List<String> list=new ArrayList<String>();
    List<String> listUrl=new ArrayList<String>();
    private static List<FoundUrl> foundURLList = new ArrayList<FoundUrl>();
    //private final CrawlerRepository crawlRepository;

	/*
	 * @Autowired public HelloController(CrawlerRepository crawlRepository) {
	 * this.crawlRepository = crawlRepository; }
	 */
	@GetMapping("/index")
	public String getHello(Model model) {
		//model.addAttribute("hello","hi");
		model.addAttribute("listSize", list.size());
		return "index";
	}
	
	@GetMapping("/home")
	public String homePage(Model model)
	{
		foundURLList.clear();
		list.clear();
		model.addAttribute("listSize", list.size());
		model.addAttribute("foundURL", "");
		model.addAttribute("nourlFound", "");
		model.addAttribute("duplicate", "");
		this.pagesVisited.clear();
		this.pagesToVisit.clear();
		return "index";
		
	}
	//@RequestMapping("/addurl")
	
	  @PostMapping("/addurl")
	  
	  public String addURL(String url,Model model) {
	  
		  System.out.println("url "+url);
	  if(list.size()==0)
	  {
		  list.add(url);
		  System.out.println("if ");
	  }
	  else
	  {
	  
	  boolean chkExists=checkList( url,model);
			
	  if(chkExists)
	  {
		  model.addAttribute("duplicate", "URL already added, please try new url");

	  }
	  else
	  {
		  list.add(url);
		  model.addAttribute("duplicate", "");
	  }
	  
	  }
	  //list.add("https://www.google.com/");
	  System.out.println("list "+list.size());
	  //System.out.println("list "+list.get(0));
	  model.addAttribute("listSize", list.size());
	  return "index";
	  
	  }
	  
	  @GetMapping("/search")

	  public String searchPage() {
		  
		
	  return "search";
	  
	  }
	  
	  @PostMapping("/searchByword")
	 
	  public String search( String search,Model model) {
		  
		  System.out.println("searchWord "+search);
		  //int list1=list.size();
		  for(int i=0;i<list.size();i++) 
		  {
	        while (this.pagesVisited.size() < MAX_PAGES_TO_SEARCH) {
	            String currentUrl;
	            Crawler leg = new Crawler();
	            FoundUrl url=new FoundUrl();
				
				  
				 
	            if (this.pagesToVisit.isEmpty()) {
	                currentUrl = list.get(i);
	                this.pagesVisited.add(list.get(i));
	            } else {
	                currentUrl = this.nextUrl();
	            }
	            
	            	leg.crawl(currentUrl);
	            
	             // Lots of stuff happening here. Look at the crawl method in
	            // SpiderLeg
	            boolean success = leg.searchForWord(search);
	            if (success) {
	                System.out.println(String.format("**Success** Word %s found at %s", search, currentUrl));
	               // students.add(currentUrl);
	               // url.setListOfStringsV2(listUrl);
	                model.addAttribute("foundURLS", addList(currentUrl));
	                model.addAttribute("urlFound", "Found");
	               // model.addAttribute("foundURL", crawlRepository.findAll());
	                break;
	            }
	            this.pagesToVisit.addAll(leg.getLinks());
	           // System.out.println("searchWord "+url.getListOfStringsV2().size());
	        }
	        System.out.println("\n**Done** Visited " + this.pagesVisited.size() + " web page(s)");
	        
	        if(foundURLList.size()<=0)
	        {
	        	model.addAttribute("urlFound", "Not Found");
	        }
	        
	        }
			return "searchResults";
	    }
	  
	  
	  public static List<FoundUrl> addList(String url) {
	       
	        	FoundUrl foundCurrent = new FoundUrl();
	        	foundCurrent.setUrlAddress(url);
	        
	        	foundURLList.add(foundCurrent);

	        return foundURLList;
	    }
	 
	  
	  public boolean checkList(String url,Model model)
	  {
	  boolean result=false;
	  for(int i=0;i<list.size();i++)
	  {
		  if(list.get(i).contains(url))
		  {
			  System.out.println("URL FOund "+list.get(i));
			  result= true;
			  return true;
		  }
		  else
		  {
			  result= false; 
		  }
		
		   
		  
	   
	  }
	  System.out.println("Record Exists "+result); 
	return result;
}

	    /**
	     * Returns the next URL to visit (in the order that they were found). We also do a check to make
	     * sure this method doesn't return a URL that has already been visited.
	     *
	     * @return
	     */
	    private String nextUrl() {
	        String nextUrl;
	        do {
	            nextUrl = this.pagesToVisit.remove(0);
	        } while (this.pagesVisited.contains(nextUrl));
	        this.pagesVisited.add(nextUrl);
	        return nextUrl;
	    }
}