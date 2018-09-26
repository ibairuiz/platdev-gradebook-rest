package com.liferay.training.space.gradebook.service.rest;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.service.CompanyService;
import com.liferay.portal.kernel.service.GroupService;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.training.space.gradebook.model.Assignment;
import com.liferay.training.space.gradebook.service.AssignmentService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;


@Component(
	immediate = true,
	service = Application.class
)
@ApplicationPath("/")
public class AssignmentRestService extends Application {
	
	private Log logger = LogFactoryUtil.getLog(AssignmentRestService.class.getName());
	
	@GET
	@Produces("text/plain")
	public String working() {
		return "El servicio está publicado correctamente.";
	}
	
	@GET
	@Path("/assignments")
	@Produces({MediaType.APPLICATION_JSON})
	public String getAssignments() {
		
		Company company;
		List<Assignment> assignments = new ArrayList<Assignment>();
		
		try {
			company = companyService.getCompanyById(PortalUtil.getDefaultCompanyId());
			List<Group> groups = groupService.getGroups(company.getCompanyId(), 0, true);
			for (Group group : groups) {
				assignments.addAll(assignmentService.getAssignmentsByGroupId(group.getGroupId()));
			}
			return JSONFactoryUtil.serialize(assignments);
			
		} catch (PortalException e) {
			logger.error(e);
			return "[{}]";
		}
	}
	@GET
	@Path("/assignment/{assignmentid}")
	@Produces({MediaType.APPLICATION_JSON})
	public String getAssignments(@PathParam("assignmentid") long assignmentId) {
		try {
			return JSONFactoryUtil.serialize(assignmentService.getAssignment(assignmentId));
		} catch (Exception e) {
			logger.error(e);
			return "{}";
		}
	}	
	public Set<Object> getSingletons() {
		return Collections.<Object>singleton(this);
	}
	
	@Reference
	AssignmentService assignmentService;
	
	@Reference
	GroupService groupService;
	
	@Reference
	CompanyService companyService;
}