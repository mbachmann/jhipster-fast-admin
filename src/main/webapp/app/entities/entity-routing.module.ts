import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'owner',
        data: { pageTitle: 'fastAdminApp.owner.home.title' },
        loadChildren: () => import('./owner/owner.module').then(m => m.OwnerModule),
      },
      {
        path: 'contact',
        data: { pageTitle: 'fastAdminApp.contact.home.title' },
        loadChildren: () => import('./contact/contact.module').then(m => m.ContactModule),
      },
      {
        path: 'contact-relation',
        data: { pageTitle: 'fastAdminApp.contactRelation.home.title' },
        loadChildren: () => import('./contact-relation/contact-relation.module').then(m => m.ContactRelationModule),
      },
      {
        path: 'contact-address',
        data: { pageTitle: 'fastAdminApp.contactAddress.home.title' },
        loadChildren: () => import('./contact-address/contact-address.module').then(m => m.ContactAddressModule),
      },
      {
        path: 'contact-group',
        data: { pageTitle: 'fastAdminApp.contactGroup.home.title' },
        loadChildren: () => import('./contact-group/contact-group.module').then(m => m.ContactGroupModule),
      },
      {
        path: 'contact-person',
        data: { pageTitle: 'fastAdminApp.contactPerson.home.title' },
        loadChildren: () => import('./contact-person/contact-person.module').then(m => m.ContactPersonModule),
      },
      {
        path: 'contact-account',
        data: { pageTitle: 'fastAdminApp.contactAccount.home.title' },
        loadChildren: () => import('./contact-account/contact-account.module').then(m => m.ContactAccountModule),
      },
      {
        path: 'contact-reminder',
        data: { pageTitle: 'fastAdminApp.contactReminder.home.title' },
        loadChildren: () => import('./contact-reminder/contact-reminder.module').then(m => m.ContactReminderModule),
      },
      {
        path: 'bank-account',
        data: { pageTitle: 'fastAdminApp.bankAccount.home.title' },
        loadChildren: () => import('./bank-account/bank-account.module').then(m => m.BankAccountModule),
      },
      {
        path: 'exchange-rate',
        data: { pageTitle: 'fastAdminApp.exchangeRate.home.title' },
        loadChildren: () => import('./exchange-rate/exchange-rate.module').then(m => m.ExchangeRateModule),
      },
      {
        path: 'catalog-category',
        data: { pageTitle: 'fastAdminApp.catalogCategory.home.title' },
        loadChildren: () => import('./catalog-category/catalog-category.module').then(m => m.CatalogCategoryModule),
      },
      {
        path: 'catalog-unit',
        data: { pageTitle: 'fastAdminApp.catalogUnit.home.title' },
        loadChildren: () => import('./catalog-unit/catalog-unit.module').then(m => m.CatalogUnitModule),
      },
      {
        path: 'value-added-tax',
        data: { pageTitle: 'fastAdminApp.valueAddedTax.home.title' },
        loadChildren: () => import('./value-added-tax/value-added-tax.module').then(m => m.ValueAddedTaxModule),
      },
      {
        path: 'catalog-product',
        data: { pageTitle: 'fastAdminApp.catalogProduct.home.title' },
        loadChildren: () => import('./catalog-product/catalog-product.module').then(m => m.CatalogProductModule),
      },
      {
        path: 'catalog-service',
        data: { pageTitle: 'fastAdminApp.catalogService.home.title' },
        loadChildren: () => import('./catalog-service/catalog-service.module').then(m => m.CatalogServiceModule),
      },
      {
        path: 'document-letter',
        data: { pageTitle: 'fastAdminApp.documentLetter.home.title' },
        loadChildren: () => import('./document-letter/document-letter.module').then(m => m.DocumentLetterModule),
      },
      {
        path: 'delivery-note',
        data: { pageTitle: 'fastAdminApp.deliveryNote.home.title' },
        loadChildren: () => import('./delivery-note/delivery-note.module').then(m => m.DeliveryNoteModule),
      },
      {
        path: 'invoice',
        data: { pageTitle: 'fastAdminApp.invoice.home.title' },
        loadChildren: () => import('./invoice/invoice.module').then(m => m.InvoiceModule),
      },
      {
        path: 'offer',
        data: { pageTitle: 'fastAdminApp.offer.home.title' },
        loadChildren: () => import('./offer/offer.module').then(m => m.OfferModule),
      },
      {
        path: 'order-confirmation',
        data: { pageTitle: 'fastAdminApp.orderConfirmation.home.title' },
        loadChildren: () => import('./order-confirmation/order-confirmation.module').then(m => m.OrderConfirmationModule),
      },
      {
        path: 'document-free-text',
        data: { pageTitle: 'fastAdminApp.documentFreeText.home.title' },
        loadChildren: () => import('./document-free-text/document-free-text.module').then(m => m.DocumentFreeTextModule),
      },
      {
        path: 'free-text',
        data: { pageTitle: 'fastAdminApp.freeText.home.title' },
        loadChildren: () => import('./free-text/free-text.module').then(m => m.FreeTextModule),
      },
      {
        path: 'signature',
        data: { pageTitle: 'fastAdminApp.signature.home.title' },
        loadChildren: () => import('./signature/signature.module').then(m => m.SignatureModule),
      },
      {
        path: 'layout',
        data: { pageTitle: 'fastAdminApp.layout.home.title' },
        loadChildren: () => import('./layout/layout.module').then(m => m.LayoutModule),
      },
      {
        path: 'document-position',
        data: { pageTitle: 'fastAdminApp.documentPosition.home.title' },
        loadChildren: () => import('./document-position/document-position.module').then(m => m.DocumentPositionModule),
      },
      {
        path: 'descriptive-document-text',
        data: { pageTitle: 'fastAdminApp.descriptiveDocumentText.home.title' },
        loadChildren: () =>
          import('./descriptive-document-text/descriptive-document-text.module').then(m => m.DescriptiveDocumentTextModule),
      },
      {
        path: 'document-text',
        data: { pageTitle: 'fastAdminApp.documentText.home.title' },
        loadChildren: () => import('./document-text/document-text.module').then(m => m.DocumentTextModule),
      },
      {
        path: 'document-invoice-workflow',
        data: { pageTitle: 'fastAdminApp.documentInvoiceWorkflow.home.title' },
        loadChildren: () =>
          import('./document-invoice-workflow/document-invoice-workflow.module').then(m => m.DocumentInvoiceWorkflowModule),
      },
      {
        path: 'isr',
        data: { pageTitle: 'fastAdminApp.isr.home.title' },
        loadChildren: () => import('./isr/isr.module').then(m => m.IsrModule),
      },
      {
        path: 'cost-unit',
        data: { pageTitle: 'fastAdminApp.costUnit.home.title' },
        loadChildren: () => import('./cost-unit/cost-unit.module').then(m => m.CostUnitModule),
      },
      {
        path: 'effort',
        data: { pageTitle: 'fastAdminApp.effort.home.title' },
        loadChildren: () => import('./effort/effort.module').then(m => m.EffortModule),
      },
      {
        path: 'project',
        data: { pageTitle: 'fastAdminApp.project.home.title' },
        loadChildren: () => import('./project/project.module').then(m => m.ProjectModule),
      },
      {
        path: 'working-hour',
        data: { pageTitle: 'fastAdminApp.workingHour.home.title' },
        loadChildren: () => import('./working-hour/working-hour.module').then(m => m.WorkingHourModule),
      },
      {
        path: 'reporting-activity',
        data: { pageTitle: 'fastAdminApp.reportingActivity.home.title' },
        loadChildren: () => import('./reporting-activity/reporting-activity.module').then(m => m.ReportingActivityModule),
      },
      {
        path: 'application-role',
        data: { pageTitle: 'fastAdminApp.applicationRole.home.title' },
        loadChildren: () => import('./application-role/application-role.module').then(m => m.ApplicationRoleModule),
      },
      {
        path: 'application-user',
        data: { pageTitle: 'fastAdminApp.applicationUser.home.title' },
        loadChildren: () => import('./application-user/application-user.module').then(m => m.ApplicationUserModule),
      },
      {
        path: 'custom-field-value',
        data: { pageTitle: 'fastAdminApp.customFieldValue.home.title' },
        loadChildren: () => import('./custom-field-value/custom-field-value.module').then(m => m.CustomFieldValueModule),
      },
      {
        path: 'custom-field',
        data: { pageTitle: 'fastAdminApp.customField.home.title' },
        loadChildren: () => import('./custom-field/custom-field.module').then(m => m.CustomFieldModule),
      },
      {
        path: 'resource-permission',
        data: { pageTitle: 'fastAdminApp.resourcePermission.home.title' },
        loadChildren: () => import('./resource-permission/resource-permission.module').then(m => m.ResourcePermissionModule),
      },
      {
        path: 'permission',
        data: { pageTitle: 'fastAdminApp.permission.home.title' },
        loadChildren: () => import('./permission/permission.module').then(m => m.PermissionModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
