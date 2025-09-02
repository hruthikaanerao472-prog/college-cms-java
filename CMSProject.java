import java.util.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

public class CMSProject {
    public static void main(String[] args) {
        System.out.println("My Content Management System Project");
        System.out.println("===================================");
        
        // making the main pages first
        Page home = new Page("Home", "/");
        Page academics = new Page("Academics", "/academics");
        Page departments = new Page("Departments", "/departments");
        Page admissions = new Page("Admissions", "/admissions");
        Page studentLife = new Page("Student Life", "/student-life");
        
        // adding tags to organize content
        academics.addTag("education");
        academics.addTag("courses");
        departments.addTag("faculty");
        departments.addTag("programs");
        admissions.addTag("enrollment");
        studentLife.addTag("campus");
        studentLife.addTag("activities");
        
        // building the website structure
        home.addChild(academics);
        home.addChild(departments);
        home.addChild(admissions);
        home.addChild(studentLife);
        
        // now adding sub-pages under academics
        Page undergraduate = new Page("Undergraduate", "/academics/undergraduate");
        Page graduate = new Page("Graduate", "/academics/graduate");
        Page courses = new Page("Courses", "/academics/courses");
        
        undergraduate.addTag("bachelors");
        undergraduate.addTag("education");
        graduate.addTag("masters");
        graduate.addTag("phd");
        courses.addTag("syllabus");
        courses.addTag("education");
        
        academics.addChild(undergraduate);
        academics.addChild(graduate);
        academics.addChild(courses);
        
        // adding departments
        Page engineering = new Page("Engineering", "/departments/engineering");
        Page business = new Page("Business", "/departments/business");
        Page arts = new Page("Arts & Sciences", "/departments/arts");
        
        engineering.addTag("technology");
        engineering.addTag("programs");
        business.addTag("management");
        business.addTag("programs");
        arts.addTag("liberal-arts");
        arts.addTag("programs");
        
        departments.addChild(engineering);
        departments.addChild(business);
        departments.addChild(arts);
        
        // more specific pages under engineering
        Page computerScience = new Page("Computer Science", "/departments/engineering/cs");
        Page mechanical = new Page("Mechanical", "/departments/engineering/mechanical");
        
        computerScience.addTag("programming");
        computerScience.addTag("technology");
        mechanical.addTag("design");
        mechanical.addTag("technology");
        
        engineering.addChild(computerScience);
        engineering.addChild(mechanical);
        
        // student life sections
        Page clubs = new Page("Clubs", "/student-life/clubs");
        Page sports = new Page("Sports", "/student-life/sports");
        Page housing = new Page("Housing", "/student-life/housing");
        
        clubs.addTag("activities");
        clubs.addTag("social");
        sports.addTag("athletics");
        sports.addTag("activities");
        housing.addTag("dormitory");
        housing.addTag("campus");
        
        studentLife.addChild(clubs);
        studentLife.addChild(sports);
        studentLife.addChild(housing);
        
        System.out.println();
        
        // showing what my program can do
        System.out.println("FEATURE 1 - Navigation Breadcrumbs:");
        System.out.println("Computer Science page: " + computerScience.getBreadcrumb());
        System.out.println("Clubs page: " + clubs.getBreadcrumb());
        System.out.println("Graduate page: " + graduate.getBreadcrumb());
        System.out.println();
        
        System.out.println("FEATURE 2 - Search by Tags:");
        System.out.println("Finding all pages with 'technology' tag:");
        List<Page> techPages = home.searchByTag("technology");
        for (Page page : techPages) {
            System.out.println("  -> " + page.getTitle() + " at " + page.getPath());
        }
        System.out.println();
        
        System.out.println("Finding all pages with 'activities' tag:");
        List<Page> activityPages = home.searchByTag("activities");
        for (Page page : activityPages) {
            System.out.println("  -> " + page.getTitle() + " at " + page.getPath());
        }
        System.out.println();
        
        System.out.println("FEATURE 3 - Recently Updated Pages:");
        List<Page> recentPages = home.getRecentlyModified(30);
        System.out.println("Pages updated in last 30 days:");
        for (Page page : recentPages) {
            System.out.println("  -> " + page.getTitle());
        }
        System.out.println();
        
        System.out.println("FEATURE 4 - Complete Website Structure:");
        showWebsiteStructure(home, 0);
        
        System.out.println();
        System.out.println("Project completed successfully!");
    }
    
    // helper method to display the hierarchy nicely
    static void showWebsiteStructure(Page page, int level) {
        String spaces = "";
        for (int i = 0; i < level; i++) {
            spaces += "  ";
        }
        System.out.println(spaces + "- " + page.getTitle() + " (" + page.getPath() + ")");
        
        for (Page child : page.getChildren()) {
            showWebsiteStructure(child, level + 1);
        }
    }
}

class Page {
    private String title;
    private String path;
    private List<String> tags;
    private Date lastModified;
    private List<Page> children;
    private Page parent;
    
    public Page(String title, String path) {
        this.title = title;
        this.path = path;
        this.tags = new ArrayList<>();
        this.lastModified = new Date();
        this.children = new ArrayList<>();
        this.parent = null;
    }
    
    // adds a child page under this page
    public void addChild(Page child) {
        if (child != null) {
            child.parent = this;
            this.children.add(child);
        }
    }
    
    // creates breadcrumb navigation like "Home > Academics > Graduate"
    public String getBreadcrumb() {
        if (parent == null) {
            return title;
        }
        return parent.getBreadcrumb() + " > " + title;
    }
    
    // adds a tag to this page for categorization
    public void addTag(String tag) {
        if (tag != null && !this.tags.contains(tag)) {
            this.tags.add(tag);
        }
    }
    
    // searches all pages recursively for a specific tag
    public List<Page> searchByTag(String tag) {
        List<Page> results = new ArrayList<>();
        
        // check if current page has the tag
        if (this.tags.contains(tag)) {
            results.add(this);
        }
        
        // check all child pages too
        for (Page child : children) {
            List<Page> childResults = child.searchByTag(tag);
            results.addAll(childResults);
        }
        
        return results;
    }
    
    // finds pages modified within last X days
    public List<Page> getRecentlyModified(int days) {
        List<Page> results = new ArrayList<>();
        LocalDateTime cutoffDate = LocalDateTime.now().minus(days, ChronoUnit.DAYS);
        
        // convert page's last modified date for comparison
        LocalDateTime pageDate = this.lastModified.toInstant()
            .atZone(ZoneId.systemDefault())
            .toLocalDateTime();
        
        if (pageDate.isAfter(cutoffDate)) {
            results.add(this);
        }
        
        // check child pages recursively
        for (Page child : children) {
            List<Page> childResults = child.getRecentlyModified(days);
            results.addAll(childResults);
        }
        
        return results;
    }
    
    // basic getters
    public String getTitle() { 
        return title; 
    }
    
    public String getPath() { 
        return path; 
    }
    
    public List<String> getTags() { 
        return new ArrayList<>(tags); 
    }
    
    public List<Page> getChildren() { 
        return new ArrayList<>(children); 
    }
}