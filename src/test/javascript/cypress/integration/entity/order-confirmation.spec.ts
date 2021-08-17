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

describe('OrderConfirmation e2e test', () => {
  const orderConfirmationPageUrl = '/order-confirmation';
  const orderConfirmationPageUrlPattern = new RegExp('/order-confirmation(\\?.*)?$');
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
    cy.intercept('GET', '/api/order-confirmations+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/order-confirmations').as('postEntityRequest');
    cy.intercept('DELETE', '/api/order-confirmations/*').as('deleteEntityRequest');
  });

  it('should load OrderConfirmations', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('order-confirmation');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('OrderConfirmation').should('exist');
    cy.url().should('match', orderConfirmationPageUrlPattern);
  });

  it('should load details OrderConfirmation page', function () {
    cy.visit(orderConfirmationPageUrl);
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        this.skip();
      }
    });
    cy.get(entityDetailsButtonSelector).first().click({ force: true });
    cy.getEntityDetailsHeading('orderConfirmation');
    cy.get(entityDetailsBackButtonSelector).click({ force: true });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', orderConfirmationPageUrlPattern);
  });

  it('should load create OrderConfirmation page', () => {
    cy.visit(orderConfirmationPageUrl);
    cy.wait('@entitiesRequest');
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('OrderConfirmation');
    cy.get(entityCreateSaveButtonSelector).should('exist');
    cy.get(entityCreateCancelButtonSelector).click({ force: true });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', orderConfirmationPageUrlPattern);
  });

  it('should load edit OrderConfirmation page', function () {
    cy.visit(orderConfirmationPageUrl);
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        this.skip();
      }
    });
    cy.get(entityEditButtonSelector).first().click({ force: true });
    cy.getEntityCreateUpdateHeading('OrderConfirmation');
    cy.get(entityCreateSaveButtonSelector).should('exist');
    cy.get(entityCreateCancelButtonSelector).click({ force: true });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', orderConfirmationPageUrlPattern);
  });

  it('should create an instance of OrderConfirmation', () => {
    cy.visit(orderConfirmationPageUrl);
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('OrderConfirmation');

    cy.get(`[data-cy="remoteId"]`).type('95170').should('have.value', '95170');

    cy.get(`[data-cy="number"]`).type('static up').should('have.value', 'static up');

    cy.get(`[data-cy="contactName"]`).type('emulation Islands 24/7').should('have.value', 'emulation Islands 24/7');

    cy.get(`[data-cy="date"]`).type('2021-08-17').should('have.value', '2021-08-17');

    cy.get(`[data-cy="periodText"]`).type('National').should('have.value', 'National');

    cy.get(`[data-cy="currency"]`).select('SZL');

    cy.get(`[data-cy="total"]`).type('35712').should('have.value', '35712');

    cy.get(`[data-cy="vatIncluded"]`).should('not.be.checked');
    cy.get(`[data-cy="vatIncluded"]`).click().should('be.checked');

    cy.get(`[data-cy="discountRate"]`).type('13099').should('have.value', '13099');

    cy.get(`[data-cy="discountType"]`).select('AMOUNT');

    cy.get(`[data-cy="language"]`).select('ENGLISH');

    cy.get(`[data-cy="pageAmount"]`).type('52526').should('have.value', '52526');

    cy.get(`[data-cy="notes"]`).type('Facilitator Concrete').should('have.value', 'Facilitator Concrete');

    cy.get(`[data-cy="status"]`).select('BILLED');

    cy.get(`[data-cy="created"]`).type('2021-08-17T09:51').should('have.value', '2021-08-17T09:51');

    cy.setFieldSelectToLastOfEntity('contact');

    cy.setFieldSelectToLastOfEntity('contactAddress');

    cy.setFieldSelectToLastOfEntity('contactPerson');

    cy.setFieldSelectToLastOfEntity('contactPrePageAddress');

    cy.setFieldSelectToLastOfEntity('layout');

    cy.setFieldSelectToLastOfEntity('layout');

    cy.get(entityCreateSaveButtonSelector).click({ force: true });
    cy.scrollTo('top', { ensureScrollable: false });
    cy.get(entityCreateSaveButtonSelector).should('not.exist');
    cy.wait('@postEntityRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(201);
    });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', orderConfirmationPageUrlPattern);
  });

  it('should delete last instance of OrderConfirmation', function () {
    cy.visit(orderConfirmationPageUrl);
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length > 0) {
        cy.get(entityTableSelector).should('have.lengthOf', response.body.length);
        cy.get(entityDeleteButtonSelector).last().click({ force: true });
        cy.getEntityDeleteDialogHeading('orderConfirmation').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', orderConfirmationPageUrlPattern);
      } else {
        this.skip();
      }
    });
  });
});
