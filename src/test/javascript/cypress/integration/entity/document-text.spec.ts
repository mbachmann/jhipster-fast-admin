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

describe('DocumentText e2e test', () => {
  const documentTextPageUrl = '/document-text';
  const documentTextPageUrlPattern = new RegExp('/document-text(\\?.*)?$');
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
    cy.intercept('GET', '/api/document-texts+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/document-texts').as('postEntityRequest');
    cy.intercept('DELETE', '/api/document-texts/*').as('deleteEntityRequest');
  });

  it('should load DocumentTexts', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('document-text');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('DocumentText').should('exist');
    cy.url().should('match', documentTextPageUrlPattern);
  });

  it('should load details DocumentText page', function () {
    cy.visit(documentTextPageUrl);
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        this.skip();
      }
    });
    cy.get(entityDetailsButtonSelector).first().click({ force: true });
    cy.getEntityDetailsHeading('documentText');
    cy.get(entityDetailsBackButtonSelector).click({ force: true });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', documentTextPageUrlPattern);
  });

  it('should load create DocumentText page', () => {
    cy.visit(documentTextPageUrl);
    cy.wait('@entitiesRequest');
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('DocumentText');
    cy.get(entityCreateSaveButtonSelector).should('exist');
    cy.get(entityCreateCancelButtonSelector).click({ force: true });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', documentTextPageUrlPattern);
  });

  it('should load edit DocumentText page', function () {
    cy.visit(documentTextPageUrl);
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        this.skip();
      }
    });
    cy.get(entityEditButtonSelector).first().click({ force: true });
    cy.getEntityCreateUpdateHeading('DocumentText');
    cy.get(entityCreateSaveButtonSelector).should('exist');
    cy.get(entityCreateCancelButtonSelector).click({ force: true });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', documentTextPageUrlPattern);
  });

  it('should create an instance of DocumentText', () => {
    cy.visit(documentTextPageUrl);
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('DocumentText');

    cy.get(`[data-cy="defaultText"]`).should('not.be.checked');
    cy.get(`[data-cy="defaultText"]`).click().should('be.checked');

    cy.get(`[data-cy="text"]`).type('XSS Berkshire granular').should('have.value', 'XSS Berkshire granular');

    cy.get(`[data-cy="language"]`).select('FRENCH');

    cy.get(`[data-cy="usage"]`).select('INTRODUCTION');

    cy.get(`[data-cy="status"]`).select('PAYMENT_REMINDER');

    cy.get(`[data-cy="type"]`).select('EMAIL');

    cy.get(`[data-cy="documentType"]`).select('INVOICE');

    cy.get(entityCreateSaveButtonSelector).click({ force: true });
    cy.scrollTo('top', { ensureScrollable: false });
    cy.get(entityCreateSaveButtonSelector).should('not.exist');
    cy.wait('@postEntityRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(201);
    });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', documentTextPageUrlPattern);
  });

  it('should delete last instance of DocumentText', function () {
    cy.visit(documentTextPageUrl);
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length > 0) {
        cy.get(entityTableSelector).should('have.lengthOf', response.body.length);
        cy.get(entityDeleteButtonSelector).last().click({ force: true });
        cy.getEntityDeleteDialogHeading('documentText').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', documentTextPageUrlPattern);
      } else {
        this.skip();
      }
    });
  });
});
