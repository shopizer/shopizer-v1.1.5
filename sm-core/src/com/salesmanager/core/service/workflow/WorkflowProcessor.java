/*
 * Licensed to csti consulting 
 * You may obtain a copy of the License at
 *
 * http://www.csticonsulting.com
 * Copyright (c) 2006-Aug 24, 2010 Consultation CS-TI inc. 
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.salesmanager.core.service.workflow;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public abstract class WorkflowProcessor {

	private List activities;

	public void doWorkflow() throws Exception {
		ProcessorContext ctx = new ProcessorContext();
		doWorkflow(ctx);
	}

	public abstract void doWorkflow(ProcessorContext ctx) throws Exception;

	public void setActivities(List activities) {
		this.activities = activities;

	}

	public List getActivities() {
		return activities;
	}

}
