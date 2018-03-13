import primary.RestClient
import com.atlassian.jira.event.issue.IssueEvent
import com.atlassian.jira.component.ComponentAccessor
import static com.atlassian.jira.issue.IssueFieldConstants.*
import com.atlassian.jira.issue.MutableIssue
import org.apache.log4j.Category
 
def Category log = Category.getInstance("com.onresolve.jira.groovy")
log.setLevel(org.apache.log4j.Level.DEBUG)

def changeLog = event.getChangeLog();


def changeHistoryManager = ComponentAccessor.getChangeHistoryManager()
def changedItem = changeHistoryManager.getAllChangeItems(event.issue)?.findAll {it.field == STATUS}?.any()
def camundaUrl = "http://localhost:9080/engine-rest";

def statusChange = event.getChangeLog()?.getRelated('ChildChangeItem').any{ it.field== STATUS}				
if (statusChange){	
    
	log.debug("hell yes!")
	//def previousStatus = changeItem.getFromString();
	def newStatus = event.issue.getStatus().getName().toString()
    def issueType = event.issue.getIssueType().getName().toString()
    def issueKey = event.issue.getKey().toString()
    log.debug("issueType:  " + issueType + "  issueKey:   " + issueKey + "  new Status:   " +  newStatus)
    
    RestClient restClient = new RestClient(camundaUrl);
    restClient.jiraTicketTransition(issueType,issueKey, newStatus);
    
	//log.debug("issueType:  " + issueType + "  issueKey:   " + issueKey + "  new Status:   " +  newStatus)
}
else{
    log.debug("Event ignored - not a transition")
    
}