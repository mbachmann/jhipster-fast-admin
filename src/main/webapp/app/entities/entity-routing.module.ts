import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'owner-my-suffix',
        data: { pageTitle: 'fastAdminApp.owner.home.title' },
        loadChildren: () => import('./owner-my-suffix/owner-my-suffix.module').then(m => m.OwnerMySuffixModule),
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
        path: 'contact-relation-my-suffix',
        data: { pageTitle: 'fastAdminApp.contactRelation.home.title' },
        loadChildren: () =>
          import('./contact-relation-my-suffix/contact-relation-my-suffix.module').then(m => m.ContactRelationMySuffixModule),
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
        path: 'contact-person-my-suffix',
        data: { pageTitle: 'fastAdminApp.contactPerson.home.title' },
        loadChildren: () => import('./contact-person-my-suffix/contact-person-my-suffix.module').then(m => m.ContactPersonMySuffixModule),
      },
      {
        path: 'role-my-suffix',
        data: { pageTitle: 'fastAdminApp.role.home.title' },
        loadChildren: () => import('./role-my-suffix/role-my-suffix.module').then(m => m.RoleMySuffixModule),
      },
      {
        path: 'contact-reminder-my-suffix',
        data: { pageTitle: 'fastAdminApp.contactReminder.home.title' },
        loadChildren: () =>
          import('./contact-reminder-my-suffix/contact-reminder-my-suffix.module').then(m => m.ContactReminderMySuffixModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
