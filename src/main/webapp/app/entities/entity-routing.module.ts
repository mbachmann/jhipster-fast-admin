import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'region-my-suffix',
        data: { pageTitle: 'fastAdminApp.region.home.title' },
        loadChildren: () => import('./region-my-suffix/region-my-suffix.module').then(m => m.RegionMySuffixModule),
      },
      {
        path: 'country-my-suffix',
        data: { pageTitle: 'fastAdminApp.country.home.title' },
        loadChildren: () => import('./country-my-suffix/country-my-suffix.module').then(m => m.CountryMySuffixModule),
      },
      {
        path: 'location-my-suffix',
        data: { pageTitle: 'fastAdminApp.location.home.title' },
        loadChildren: () => import('./location-my-suffix/location-my-suffix.module').then(m => m.LocationMySuffixModule),
      },
      {
        path: 'department-my-suffix',
        data: { pageTitle: 'fastAdminApp.department.home.title' },
        loadChildren: () => import('./department-my-suffix/department-my-suffix.module').then(m => m.DepartmentMySuffixModule),
      },
      {
        path: 'task-my-suffix',
        data: { pageTitle: 'fastAdminApp.task.home.title' },
        loadChildren: () => import('./task-my-suffix/task-my-suffix.module').then(m => m.TaskMySuffixModule),
      },
      {
        path: 'employee-my-suffix',
        data: { pageTitle: 'fastAdminApp.employee.home.title' },
        loadChildren: () => import('./employee-my-suffix/employee-my-suffix.module').then(m => m.EmployeeMySuffixModule),
      },
      {
        path: 'job-my-suffix',
        data: { pageTitle: 'fastAdminApp.job.home.title' },
        loadChildren: () => import('./job-my-suffix/job-my-suffix.module').then(m => m.JobMySuffixModule),
      },
      {
        path: 'job-history-my-suffix',
        data: { pageTitle: 'fastAdminApp.jobHistory.home.title' },
        loadChildren: () => import('./job-history-my-suffix/job-history-my-suffix.module').then(m => m.JobHistoryMySuffixModule),
      },
      {
        path: 'contact-my-suffix',
        data: { pageTitle: 'fastAdminApp.contact.home.title' },
        loadChildren: () => import('./contact-my-suffix/contact-my-suffix.module').then(m => m.ContactMySuffixModule),
      },
      {
        path: 'custom-field-my-suffix',
        data: { pageTitle: 'fastAdminApp.customField.home.title' },
        loadChildren: () => import('./custom-field-my-suffix/custom-field-my-suffix.module').then(m => m.CustomFieldMySuffixModule),
      },
      {
        path: 'contact-address-my-suffix',
        data: { pageTitle: 'fastAdminApp.contactAddress.home.title' },
        loadChildren: () =>
          import('./contact-address-my-suffix/contact-address-my-suffix.module').then(m => m.ContactAddressMySuffixModule),
      },
      {
        path: 'contact-group-my-suffix',
        data: { pageTitle: 'fastAdminApp.contactGroup.home.title' },
        loadChildren: () => import('./contact-group-my-suffix/contact-group-my-suffix.module').then(m => m.ContactGroupMySuffixModule),
      },
      {
        path: 'permission-my-suffix',
        data: { pageTitle: 'fastAdminApp.permission.home.title' },
        loadChildren: () => import('./permission-my-suffix/permission-my-suffix.module').then(m => m.PermissionMySuffixModule),
      },
      {
        path: 'role-my-suffix',
        data: { pageTitle: 'fastAdminApp.role.home.title' },
        loadChildren: () => import('./role-my-suffix/role-my-suffix.module').then(m => m.RoleMySuffixModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
