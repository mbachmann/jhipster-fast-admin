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

describe('DocumentInvoiceWorkflow e2e test', () => {
  const documentInvoiceWorkflowPageUrl = '/document-invoice-workflow-fa';
  const documentInvoiceWorkflowPageUrlPattern = new RegExp('/document-invoice-workflow-fa(\\?.*)?$');
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
    cy.intercept('GET', '/api/document-invoice-workflows+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/document-invoice-workflows').as('postEntityRequest');
    cy.intercept('DELETE', '/api/document-invoice-workflows/*').as('deleteEntityRequest');
  });

  it('should load DocumentInvoiceWorkflows', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('document-invoice-workflow-fa');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('DocumentInvoiceWorkflow').should('exist');
    cy.url().should('match', documentInvoiceWorkflowPageUrlPattern);
  });

  it('should load details DocumentInvoiceWorkflow page', function () {
    cy.visit(documentInvoiceWorkflowPageUrl);
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        this.skip();
      }
    });
    cy.get(entityDetailsButtonSelector).first().click({ force: true });
    cy.getEntityDetailsHeading('documentInvoiceWorkflow');
    cy.get(entityDetailsBackButtonSelector).click({ force: true });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', documentInvoiceWorkflowPageUrlPattern);
  });

  it('should load create DocumentInvoiceWorkflow page', () => {
    cy.visit(documentInvoiceWorkflowPageUrl);
    cy.wait('@entitiesRequest');
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('DocumentInvoiceWorkflow');
    cy.get(entityCreateSaveButtonSelector).should('exist');
    cy.get(entityCreateCancelButtonSelector).click({ force: true });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', documentInvoiceWorkflowPageUrlPattern);
  });

  it('should load edit DocumentInvoiceWorkflow page', function () {
    cy.visit(documentInvoiceWorkflowPageUrl);
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        this.skip();
      }
    });
    cy.get(entityEditButtonSelector).first().click({ force: true });
    cy.getEntityCreateUpdateHeading('DocumentInvoiceWorkflow');
    cy.get(entityCreateSaveButtonSelector).should('exist');
    cy.get(entityCreateCancelButtonSelector).click({ force: true });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', documentInvoiceWorkflowPageUrlPattern);
  });

  it('should create an instance of DocumentInvoiceWorkflow', () => {
    cy.visit(documentInvoiceWorkflowPageUrl);
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('DocumentInvoiceWorkflow');

    cy.get(`[data-cy="active"]`).should('not.be.checked');
    cy.get(`[data-cy="active"]`).click().should('be.checked');

    cy.get(`[data-cy="status"]`).select('THIRD_REMINDER');

    cy.get(`[data-cy="overdueDays"]`).type('7254').should('have.value', '7254');

    cy.get(`[data-cy="action"]`).select('REMIND_CONTACT_BY_EMAIL');

    cy.get(`[data-cy="contactEmail"]`).type('deposit Games').should('have.value', 'deposit Games');

    cy.get(`[data-cy="speed"]`).select('ECONOMY');

    cy.get(entityCreateSaveButtonSelector).click({ force: true });
    cy.scrollTo('top', { ensureScrollable: false });
    cy.get(entityCreateSaveButtonSelector).should('not.exist');
    cy.wait('@postEntityRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(201);
    });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', documentInvoiceWorkflowPageUrlPattern);
  });

  it('should delete last instance of DocumentInvoiceWorkflow', function () {
    cy.visit(documentInvoiceWorkflowPageUrl);
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length > 0) {
        cy.get(entityTableSelector).should('have.lengthOf', response.body.length);
        cy.get(entityDeleteButtonSelector).last().click({ force: true });
        cy.getEntityDeleteDialogHeading('documentInvoiceWorkflow').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', documentInvoiceWorkflowPageUrlPattern);
      } else {
        this.skip();
      }
    });
  });
});
