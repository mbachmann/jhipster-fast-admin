import { entityItemSelector } from '../../support/commands';
import {
  entityTableSelector,
  entityDetailsButtonSelector,
  entityDetailsBackButtonSelector,
  entityCreateButtonSelector,
  entityCreateSaveButtonSelector,
  entityCreateCancelButtonSelector,
  entityEditButtonSelector,
  entityDeleteButtonSelector,
  entityConfirmDeleteButtonSelector,
} from '../../support/entity';

describe('Invoice e2e test', () => {
  const invoicePageUrl = '/invoice';
  const invoicePageUrlPattern = new RegExp('/invoice(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/invoices+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/invoices').as('postEntityRequest');
    cy.intercept('DELETE', '/api/invoices/*').as('deleteEntityRequest');
  });

  it('should load Invoices', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('invoice');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Invoice').should('exist');
    cy.url().should('match', invoicePageUrlPattern);
  });

  it('should load details Invoice page', function () {
    cy.visit(invoicePageUrl);
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        this.skip();
      }
    });
    cy.get(entityDetailsButtonSelector).first().click({ force: true });
    cy.getEntityDetailsHeading('invoice');
    cy.get(entityDetailsBackButtonSelector).click({ force: true });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', invoicePageUrlPattern);
  });

  it('should load create Invoice page', () => {
    cy.visit(invoicePageUrl);
    cy.wait('@entitiesRequest');
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('Invoice');
    cy.get(entityCreateSaveButtonSelector).should('exist');
    cy.get(entityCreateCancelButtonSelector).click({ force: true });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', invoicePageUrlPattern);
  });

  it('should load edit Invoice page', function () {
    cy.visit(invoicePageUrl);
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        this.skip();
      }
    });
    cy.get(entityEditButtonSelector).first().click({ force: true });
    cy.getEntityCreateUpdateHeading('Invoice');
    cy.get(entityCreateSaveButtonSelector).should('exist');
    cy.get(entityCreateCancelButtonSelector).click({ force: true });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', invoicePageUrlPattern);
  });

  it('should create an instance of Invoice', () => {
    cy.visit(invoicePageUrl);
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('Invoice');

    cy.get(`[data-cy="remoteId"]`).type('44021').should('have.value', '44021');

    cy.get(`[data-cy="number"]`).type('National').should('have.value', 'National');

    cy.get(`[data-cy="contactName"]`).type('Metal Knoll').should('have.value', 'Metal Knoll');

    cy.get(`[data-cy="date"]`).type('2021-08-17').should('have.value', '2021-08-17');

    cy.get(`[data-cy="due"]`).type('2021-08-16').should('have.value', '2021-08-16');

    cy.get(`[data-cy="periodFrom"]`).type('2021-08-16').should('have.value', '2021-08-16');

    cy.get(`[data-cy="periodTo"]`).type('2021-08-16').should('have.value', '2021-08-16');

    cy.get(`[data-cy="periodText"]`).type('microchip').should('have.value', 'microchip');

    cy.get(`[data-cy="currency"]`).select('XFU');

    cy.get(`[data-cy="total"]`).type('31458').should('have.value', '31458');

    cy.get(`[data-cy="vatIncluded"]`).should('not.be.checked');
    cy.get(`[data-cy="vatIncluded"]`).click().should('be.checked');

    cy.get(`[data-cy="discountRate"]`).type('84405').should('have.value', '84405');

    cy.get(`[data-cy="discountType"]`).select('IN_PERCENT');

    cy.get(`[data-cy="cashDiscountRate"]`).type('97053').should('have.value', '97053');

    cy.get(`[data-cy="cashDiscountDate"]`).type('2021-08-17').should('have.value', '2021-08-17');

    cy.get(`[data-cy="totalPaid"]`).type('45538').should('have.value', '45538');

    cy.get(`[data-cy="paidDate"]`).type('connect Kip').should('have.value', 'connect Kip');

    cy.get(`[data-cy="isrPosition"]`).select('LAST_PAGE');

    cy.get(`[data-cy="isrReferenceNumber"]`).type('Guernsey deposit').should('have.value', 'Guernsey deposit');

    cy.get(`[data-cy="paymentLinkPaypal"]`).should('not.be.checked');
    cy.get(`[data-cy="paymentLinkPaypal"]`).click().should('be.checked');

    cy.get(`[data-cy="paymentLinkPaypalUrl"]`).type('harness system').should('have.value', 'harness system');

    cy.get(`[data-cy="paymentLinkPostfinance"]`).should('not.be.checked');
    cy.get(`[data-cy="paymentLinkPostfinance"]`).click().should('be.checked');

    cy.get(`[data-cy="paymentLinkPostfinanceUrl"]`).type('Village').should('have.value', 'Village');

    cy.get(`[data-cy="paymentLinkPayrexx"]`).should('not.be.checked');
    cy.get(`[data-cy="paymentLinkPayrexx"]`).click().should('be.checked');

    cy.get(`[data-cy="paymentLinkPayrexxUrl"]`).type('Oklahoma').should('have.value', 'Oklahoma');

    cy.get(`[data-cy="paymentLinkSmartcommerce"]`).should('not.be.checked');
    cy.get(`[data-cy="paymentLinkSmartcommerce"]`).click().should('be.checked');

    cy.get(`[data-cy="paymentLinkSmartcommerceUrl"]`).type('and Principal').should('have.value', 'and Principal');

    cy.get(`[data-cy="language"]`).select('ITALIAN');

    cy.get(`[data-cy="pageAmount"]`).type('2359').should('have.value', '2359');

    cy.get(`[data-cy="notes"]`).type('connect back-end Chair').should('have.value', 'connect back-end Chair');

    cy.get(`[data-cy="status"]`).select('THIRD_REMINDER');

    cy.get(`[data-cy="created"]`).type('2021-08-17T16:42').should('have.value', '2021-08-17T16:42');

    cy.setFieldSelectToLastOfEntity('contact');

    cy.setFieldSelectToLastOfEntity('contactAddress');

    cy.setFieldSelectToLastOfEntity('contactPerson');

    cy.setFieldSelectToLastOfEntity('contactPrePageAddress');

    cy.setFieldSelectToLastOfEntity('layout');

    cy.setFieldSelectToLastOfEntity('signature');

    cy.setFieldSelectToLastOfEntity('bankAccount');

    cy.setFieldSelectToLastOfEntity('isr');

    cy.get(entityCreateSaveButtonSelector).click({ force: true });
    cy.scrollTo('top', { ensureScrollable: false });
    cy.get(entityCreateSaveButtonSelector).should('not.exist');
    cy.wait('@postEntityRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(201);
    });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', invoicePageUrlPattern);
  });

  it('should delete last instance of Invoice', function () {
    cy.visit(invoicePageUrl);
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length > 0) {
        cy.get(entityTableSelector).should('have.lengthOf', response.body.length);
        cy.get(entityDeleteButtonSelector).last().click({ force: true });
        cy.getEntityDeleteDialogHeading('invoice').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', invoicePageUrlPattern);
      } else {
        this.skip();
      }
    });
  });
});
