package gr.isp.springbootapplication.web;

import gr.isp.springbootapplication.entity.Advert;
import gr.isp.springbootapplication.entity.SessionUser;
import gr.isp.springbootapplication.repository.AdvertRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
public class AppController {


    @Autowired // This means to get the bean called userRepository
    // Which is auto-generated by Spring, we will use it to handle the data
    private AdvertRepository advertRepository;

    @GetMapping("/login")
    public String login(Model model,
                        HttpSession session
    ) {
        if (!(session.getAttribute("userEmail") == null)){
            String email = session.getAttribute("userEmail").toString();
            session.removeAttribute("userEmail");
            model.addAttribute("email", email);
        }
        return "login";
    }

    @GetMapping({"/"})
    public String mainPage(Model model, Authentication auth, HttpServletRequest request) {
        SessionUser user = getSessionUser();
        String role = findRole(request);
        if(user != null){
            String companyName = user.getCompanyName();
            model.addAttribute("companyName", companyName);
        }
        model.addAttribute("role",role);
        Iterable<Advert> adverts = advertRepository.findByStatus("Visible");
        List<Advert> advertArray = new ArrayList<Advert>();
        for (Advert ad: adverts) {
            advertArray.add(ad);
        }
        model.addAttribute("advertArray",advertArray);

        return "mainPage";
    }

    @RequestMapping(value="/view/{id}", method=RequestMethod.GET)
    public String viewAdvertGet(Model model,
                             @PathVariable String id
    ) {
            long idLong = 0;
            if (!(id.isEmpty())) {
                idLong = Long.parseLong(id);
            }
            Advert advert = advertRepository.findFirstById(idLong);
            model.addAttribute("advert", advert);
            return "viewAdvert";
    }

    //roles: user, admin and visitor (has not signed in)
    private String findRole(HttpServletRequest request){
        String role = "";
        if (request.getUserPrincipal() != null) {
            if (request.isUserInRole("ROLE_ADMIN")) {
                role = "admin";
            }
            else if (request.isUserInRole("ROLE_USER")) {
                role = "user";
            }
        }
        else {
            role = "visitor";
        }
        return role;
    }

    private SessionUser getSessionUser (){
        SecurityContext securityContext = SecurityContextHolder.getContext();
        SessionUser user = null;
        if( SecurityContextHolder.getContext().getAuthentication() != null && SecurityContextHolder.getContext().getAuthentication().isAuthenticated() && !(SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken) ){
            user = (SessionUser) securityContext.getAuthentication().getPrincipal();
        }
        return user;
    }

}

