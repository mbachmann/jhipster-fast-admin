import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'owner-fa',
        data: { pageTitle: 'fastAdminApp.owner.home.title' },
        loadChildren: () => import('./owner-fa/owner-fa.module').then(m => m.OwnerFaModule),
      },
      {
        path: 'contact-fa',
        data: { pageTitle: 'fastAdminApp.contact.home.title' },
        loadChildren: () => import('./contact-fa/contact-fa.module').then(m => m.ContactFaModule),
      },
      {
        path: 'custom-field-fa',
        data: { pageTitle: 'fastAdminApp.customField.home.title' },
        loadChildren: () => import('./custom-field-fa/custom-field-fa.module').then(m => m.CustomFieldFaModule),
      },
      {
        path: 'contact-relation-fa',
        data: { pageTitle: 'fastAdminApp.contactRelation.home.title' },
        loadChildren: () => import('./contact-relation-fa/contact-relation-fa.module').then(m => m.ContactRelationFaModule),
      },
      {
        path: 'contact-address-fa',
        data: { pageTitle: 'fastAdminApp.contactAddress.home.title' },
        loadChildren: () => import('./contact-address-fa/contact-address-fa.module').then(m => m.ContactAddressFaModule),
      },
      {
        path: 'contact-group-fa',
        data: { pageTitle: 'fastAdminApp.contactGroup.home.title' },
        loadChildren: () => import('./contact-group-fa/contact-group-fa.module').then(m => m.ContactGroupFaModule),
      },
      {
        path: 'permission-fa',
        data: { pageTitle: 'fastAdminApp.permission.home.title' },
        loadChildren: () => import('./permission-fa/permission-fa.module').then(m => m.PermissionFaModule),
      },
      {
        path: 'contact-person-fa',
        data: { pageTitle: 'fastAdminApp.contactPerson.home.title' },
        loadChildren: () => import('./contact-person-fa/contact-person-fa.module').then(m => m.ContactPersonFaModule),
      },
      {
        path: 'role-fa',
        data: { pageTitle: 'fastAdminApp.role.home.title' },
        loadChildren: () => import('./role-fa/role-fa.module').then(m => m.RoleFaModule),
      },
      {
        path: 'contact-reminder-fa',
        data: { pageTitle: 'fastAdminApp.contactReminder.home.title' },
        loadChildren: () => import('./contact-reminder-fa/contact-reminder-fa.module').then(m => m.ContactReminderFaModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
