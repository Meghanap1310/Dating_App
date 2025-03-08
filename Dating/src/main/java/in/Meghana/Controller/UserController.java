package in.Meghana.Controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import in.Meghana.Entity.Notification;
import in.Meghana.Entity.User;
import in.Meghana.Repository.UserRepo;
import in.Meghana.Service.NotificationService;
import in.Meghana.Service.UserService;
import in.Meghana.util.EmailUtil;
import jakarta.servlet.http.HttpSession;


@Controller
@RequestMapping("/users")
public class UserController {
	@Autowired
	
private UserService ref;
	
	@Autowired
	private UserRepo repo;
	
	@Autowired
	private EmailUtil emailUtil;
	
	@GetMapping("/")
	public String home()
	{
		return "index";
	}
	
	@GetMapping("/register")
	public String showRegisterPage()
	{
		return "register";
	}
	
	
	@PostMapping("/register")
	public String registerUser(@ModelAttribute User  u)
	{
		System.out.println("hi");
		ref.saveUser(u);
		return "redirect:/users/login";
	}
	
	@GetMapping("/login")
	public String showLoginPage()
	{
		return "login";
	}
	
	
	@PostMapping("/login")
	public String LoginUser(@RequestParam String email,@RequestParam String password,Model model,HttpSession session)
	{
		System.out.println("1");
		 User u= ref.getData(email, password);
		 
		 session.setAttribute("id", u.getId());
		 session.setAttribute("name", u.getName());
		 if(u!=null)
		 {
		 
		 model.addAttribute("user", u);
		 List<User> li= ref.findMatches(u.getId());
		 System.out.println("matches found:" + li.size());
		 model.addAttribute("matches", li != null ? li : new ArrayList<>());

		// model.addAttribute("matches", li);
		return "dashboard";
		 }
		 else
		 {
			 model.addAttribute("error","invalid User");
			 return "login";
		 }
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	//endpoint to add a new user
	@PostMapping("/add")
	public User addUser(@RequestBody User user)
	{
		return ref.saveUser(user);
	}
	//endpoint to recommendations based on the logged-in User
	
	@GetMapping("/{id}/matches")
	public List<User> getMatches(@PathVariable Long id,Model m)
	
	{
		System.out.println("Inside getMatches() method ID:"+id);
		 List<User> matches=ref.findMatches(id);
		 System.out.println("Matches found" + matches.size());
		 m.addAttribute("matches", matches);
		return matches;
	}
	
	@GetMapping("/all")
	public List<User> getAllMatches()
	{
		return ref.getAllData();
	}
	
	
	
	
	
	
	
	
	@Autowired
	private NotificationService ref1;
	
	//after clicking on the accept
	@PostMapping("/interest")
	public String saveInterest(@RequestParam Long id, HttpSession se) {
{
		System.out.println("interest id"+id);//interested Id
		Long senderId = (Long) se.getAttribute("id");//user Login id
		String name= (String) se.getAttribute("name");
		
		    Long receiverId = id; // Person being accepted

		    Notification notification = new Notification();
	    
		    User user = repo.findById(receiverId).get();
	    System.out.println(user);
//		    
		    notification.setSender(senderId);
	    notification.setReceiver(receiverId);
		    notification.setMessage(name +" showed interest on you");
		    notification.setTime(LocalDateTime.now());
		    
		    ref1.create(notification);
		    
		    String to= user.getEmail();
		    String subject = "Dating Application";
		    String[] messages = {
		    	   name + " is interested in you! 💕",
		    	   name + " just made the first move! 😉",
		    	   name + " thinks you're absolutely irresistible! 🔥",
		    	   name + " wants to get to know you better! 💌"
		    	};
		    	String body = messages[new Random().nextInt(messages.length)];
		    
		    emailUtil.sendMessage(to, subject, body);

		return "redirect:/dashboard";
}
	}
	
	
	
	@GetMapping("/notification")
	public String getAllNotification(HttpSession se, Model m) {
	    Long id = (Long) se.getAttribute("id");

	    if (id != null) {
	        List<Notification> notifications = ref1.getAllNotifications(id);

	        // Prepare list of message and time
	        List<Map<String, Object>> notificationData = new ArrayList<>();

	        for (Notification notification : notifications) {
	            Map<String, Object> data = new HashMap<>();
	            data.put("message", notification.getMessage());  // Store message
	            data.put("time", notification.getTime());  // Store timestamp
	           
	            data.put("sender",notification.getSender());
	            notificationData.add(data);
	        }

	        m.addAttribute("notifications", notificationData);
	    }

	    return "notification"; // Thymeleaf or JSP page name
	}

	
	@GetMapping("/profile/{id}")
	public String showProfile(@PathVariable Long id, Model model) {
         User user= ref.getById(id);
		if(user!=null)
		{
		model.addAttribute("user", user);
		}
		else
		{
			 System.out.println("User not found for ID: " + id);
		        model.addAttribute("error", "User not found");
			
		}
		
	    return "profile";
		
	}

	@PostMapping("/backinterested")
	public String returnInterest(@RequestParam Long id,HttpSession se)
	{
		Long senderId = (Long) se.getAttribute("id");
		String name = (String) se.getAttribute("name");
		
        User user= ref.getById(id);
        Notification notification = new Notification();
        notification.setSender(senderId);
        notification.setReceiver(id);
        notification.setMessage(name + " showed interest in you!");
        notification.setTime(LocalDateTime.now());

        ref1.create(notification);
        if(user!=null)
        {
        	// Send email
            String to = user.getEmail();
            String subject = "Dating Application";
            String[] messages = {
                name + " wants to get to know you better! 💌",
                name + "looks like someone got a date!",
                name + "Got approved by the one you wanted!"

            };        
        String body = messages[new Random().nextInt(messages.length)];

        emailUtil.sendMessage(to, subject, body);
		
		
        }
        System.out.println("hi");
        return "redirect:/users/dashboard";	
	}
	@GetMapping("/dashboard")
	public String showDashboard() {
	    return "dashboard"; 
	}

}


