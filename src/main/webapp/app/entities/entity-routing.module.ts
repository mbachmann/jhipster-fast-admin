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
        path: 'contact-person-fa',
        data: { pageTitle: 'fastAdminApp.contactPerson.home.title' },
        loadChildren: () => import('./contact-person-fa/contact-person-fa.module').then(m => m.ContactPersonFaModule),
      },
      {
        path: 'contact-account-fa',
        data: { pageTitle: 'fastAdminApp.contactAccount.home.title' },
        loadChildren: () => import('./contact-account-fa/contact-account-fa.module').then(m => m.ContactAccountFaModule),
      },
      {
        path: 'contact-reminder-fa',
        data: { pageTitle: 'fastAdminApp.contactReminder.home.title' },
        loadChildren: () => import('./contact-reminder-fa/contact-reminder-fa.module').then(m => m.ContactReminderFaModule),
      },
      {
        path: 'bank-account-fa',
        data: { pageTitle: 'fastAdminApp.bankAccount.home.title' },
        loadChildren: () => import('./bank-account-fa/bank-account-fa.module').then(m => m.BankAccountFaModule),
      },
      {
        path: 'exchange-rate-fa',
        data: { pageTitle: 'fastAdminApp.exchangeRate.home.title' },
        loadChildren: () => import('./exchange-rate-fa/exchange-rate-fa.module').then(m => m.ExchangeRateFaModule),
      },
      {
        path: 'catalog-category-fa',
        data: { pageTitle: 'fastAdminApp.catalogCategory.home.title' },
        loadChildren: () => import('./catalog-category-fa/catalog-category-fa.module').then(m => m.CatalogCategoryFaModule),
      },
      {
        path: 'catalog-unit-fa',
        data: { pageTitle: 'fastAdminApp.catalogUnit.home.title' },
        loadChildren: () => import('./catalog-unit-fa/catalog-unit-fa.module').then(m => m.CatalogUnitFaModule),
      },
      {
        path: 'vat-fa',
        data: { pageTitle: 'fastAdminApp.vat.home.title' },
        loadChildren: () => import('./vat-fa/vat-fa.module').then(m => m.VatFaModule),
      },
      {
        path: 'catalog-product-fa',
        data: { pageTitle: 'fastAdminApp.catalogProduct.home.title' },
        loadChildren: () => import('./catalog-product-fa/catalog-product-fa.module').then(m => m.CatalogProductFaModule),
      },
      {
        path: 'catalog-service-fa',
        data: { pageTitle: 'fastAdminApp.catalogService.home.title' },
        loadChildren: () => import('./catalog-service-fa/catalog-service-fa.module').then(m => m.CatalogServiceFaModule),
      },
      {
        path: 'document-letter-fa',
        data: { pageTitle: 'fastAdminApp.documentLetter.home.title' },
        loadChildren: () => import('./document-letter-fa/document-letter-fa.module').then(m => m.DocumentLetterFaModule),
      },
      {
        path: 'delivery-note-fa',
        data: { pageTitle: 'fastAdminApp.deliveryNote.home.title' },
        loadChildren: () => import('./delivery-note-fa/delivery-note-fa.module').then(m => m.DeliveryNoteFaModule),
      },
      {
        path: 'invoice-fa',
        data: { pageTitle: 'fastAdminApp.invoice.home.title' },
        loadChildren: () => import('./invoice-fa/invoice-fa.module').then(m => m.InvoiceFaModule),
      },
      {
        path: 'offer-fa',
        data: { pageTitle: 'fastAdminApp.offer.home.title' },
        loadChildren: () => import('./offer-fa/offer-fa.module').then(m => m.OfferFaModule),
      },
      {
        path: 'order-confirmation-fa',
        data: { pageTitle: 'fastAdminApp.orderConfirmation.home.title' },
        loadChildren: () => import('./order-confirmation-fa/order-confirmation-fa.module').then(m => m.OrderConfirmationFaModule),
      },
      {
        path: 'document-free-text-fa',
        data: { pageTitle: 'fastAdminApp.documentFreeText.home.title' },
        loadChildren: () => import('./document-free-text-fa/document-free-text-fa.module').then(m => m.DocumentFreeTextFaModule),
      },
      {
        path: 'free-text-fa',
        data: { pageTitle: 'fastAdminApp.freeText.home.title' },
        loadChildren: () => import('./free-text-fa/free-text-fa.module').then(m => m.FreeTextFaModule),
      },
      {
        path: 'signature-fa',
        data: { pageTitle: 'fastAdminApp.signature.home.title' },
        loadChildren: () => import('./signature-fa/signature-fa.module').then(m => m.SignatureFaModule),
      },
      {
        path: 'layout-fa',
        data: { pageTitle: 'fastAdminApp.layout.home.title' },
        loadChildren: () => import('./layout-fa/layout-fa.module').then(m => m.LayoutFaModule),
      },
      {
        path: 'document-position-fa',
        data: { pageTitle: 'fastAdminApp.documentPosition.home.title' },
        loadChildren: () => import('./document-position-fa/document-position-fa.module').then(m => m.DocumentPositionFaModule),
      },
      {
        path: 'descriptive-document-text-fa',
        data: { pageTitle: 'fastAdminApp.descriptiveDocumentText.home.title' },
        loadChildren: () =>
          import('./descriptive-document-text-fa/descriptive-document-text-fa.module').then(m => m.DescriptiveDocumentTextFaModule),
      },
      {
        path: 'document-text-fa',
        data: { pageTitle: 'fastAdminApp.documentText.home.title' },
        loadChildren: () => import('./document-text-fa/document-text-fa.module').then(m => m.DocumentTextFaModule),
      },
      {
        path: 'document-invoice-workflow-fa',
        data: { pageTitle: 'fastAdminApp.documentInvoiceWorkflow.home.title' },
        loadChildren: () =>
          import('./document-invoice-workflow-fa/document-invoice-workflow-fa.module').then(m => m.DocumentInvoiceWorkflowFaModule),
      },
      {
        path: 'isr-fa',
        data: { pageTitle: 'fastAdminApp.isr.home.title' },
        loadChildren: () => import('./isr-fa/isr-fa.module').then(m => m.IsrFaModule),
      },
      {
        path: 'activity-fa',
        data: { pageTitle: 'fastAdminApp.activity.home.title' },
        loadChildren: () => import('./activity-fa/activity-fa.module').then(m => m.ActivityFaModule),
      },
      {
        path: 'cost-unit-fa',
        data: { pageTitle: 'fastAdminApp.costUnit.home.title' },
        loadChildren: () => import('./cost-unit-fa/cost-unit-fa.module').then(m => m.CostUnitFaModule),
      },
      {
        path: 'effort-fa',
        data: { pageTitle: 'fastAdminApp.effort.home.title' },
        loadChildren: () => import('./effort-fa/effort-fa.module').then(m => m.EffortFaModule),
      },
      {
        path: 'project-fa',
        data: { pageTitle: 'fastAdminApp.project.home.title' },
        loadChildren: () => import('./project-fa/project-fa.module').then(m => m.ProjectFaModule),
      },
      {
        path: 'working-hour-fa',
        data: { pageTitle: 'fastAdminApp.workingHour.home.title' },
        loadChildren: () => import('./working-hour-fa/working-hour-fa.module').then(m => m.WorkingHourFaModule),
      },
      {
        path: 'application-role-fa',
        data: { pageTitle: 'fastAdminApp.applicationRole.home.title' },
        loadChildren: () => import('./application-role-fa/application-role-fa.module').then(m => m.ApplicationRoleFaModule),
      },
      {
        path: 'application-user-fa',
        data: { pageTitle: 'fastAdminApp.applicationUser.home.title' },
        loadChildren: () => import('./application-user-fa/application-user-fa.module').then(m => m.ApplicationUserFaModule),
      },
      {
        path: 'custom-field-value-fa',
        data: { pageTitle: 'fastAdminApp.customFieldValue.home.title' },
        loadChildren: () => import('./custom-field-value-fa/custom-field-value-fa.module').then(m => m.CustomFieldValueFaModule),
      },
      {
        path: 'custom-field-fa',
        data: { pageTitle: 'fastAdminApp.customField.home.title' },
        loadChildren: () => import('./custom-field-fa/custom-field-fa.module').then(m => m.CustomFieldFaModule),
      },
      {
        path: 'resource-permission-fa',
        data: { pageTitle: 'fastAdminApp.resourcePermission.home.title' },
        loadChildren: () => import('./resource-permission-fa/resource-permission-fa.module').then(m => m.ResourcePermissionFaModule),
      },
      {
        path: 'permission-fa',
        data: { pageTitle: 'fastAdminApp.permission.home.title' },
        loadChildren: () => import('./permission-fa/permission-fa.module').then(m => m.PermissionFaModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
