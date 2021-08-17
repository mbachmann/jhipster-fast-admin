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

describe('DescriptiveDocumentText e2e test', () => {
  const descriptiveDocumentTextPageUrl = '/descriptive-document-text';
  const descriptiveDocumentTextPageUrlPattern = new RegExp('/descriptive-document-text(\\?.*)?$');
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
    cy.intercept('GET', '/api/descriptive-document-texts+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/descriptive-document-texts').as('postEntityRequest');
    cy.intercept('DELETE', '/api/descriptive-document-texts/*').as('deleteEntityRequest');
  });

  it('should load DescriptiveDocumentTexts', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('descriptive-document-text');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('DescriptiveDocumentText').should('exist');
    cy.url().should('match', descriptiveDocumentTextPageUrlPattern);
  });

  it('should load details DescriptiveDocumentText page', function () {
    cy.visit(descriptiveDocumentTextPageUrl);
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        this.skip();
      }
    });
    cy.get(entityDetailsButtonSelector).first().click({ force: true });
    cy.getEntityDetailsHeading('descriptiveDocumentText');
    cy.get(entityDetailsBackButtonSelector).click({ force: true });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', descriptiveDocumentTextPageUrlPattern);
  });

  it('should load create DescriptiveDocumentText page', () => {
    cy.visit(descriptiveDocumentTextPageUrl);
    cy.wait('@entitiesRequest');
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('DescriptiveDocumentText');
    cy.get(entityCreateSaveButtonSelector).should('exist');
    cy.get(entityCreateCancelButtonSelector).click({ force: true });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', descriptiveDocumentTextPageUrlPattern);
  });

  it('should load edit DescriptiveDocumentText page', function () {
    cy.visit(descriptiveDocumentTextPageUrl);
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        this.skip();
      }
    });
    cy.get(entityEditButtonSelector).first().click({ force: true });
    cy.getEntityCreateUpdateHeading('DescriptiveDocumentText');
    cy.get(entityCreateSaveButtonSelector).should('exist');
    cy.get(entityCreateCancelButtonSelector).click({ force: true });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', descriptiveDocumentTextPageUrlPattern);
  });

  it('should create an instance of DescriptiveDocumentText', () => {
    cy.visit(descriptiveDocumentTextPageUrl);
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('DescriptiveDocumentText');

    cy.get(`[data-cy="title"]`).type('Berkshire Tools Oklahoma').should('have.value', 'Berkshire Tools Oklahoma');

    cy.get(`[data-cy="introduction"]`).type('Cloned').should('have.value', 'Cloned');

    cy.get(`[data-cy="conditions"]`).type('Mississippi Kentucky Books').should('have.value', 'Mississippi Kentucky Books');

    cy.get(`[data-cy="status"]`).select('DEFAULT');

    cy.setFieldSelectToLastOfEntity('deliveryNote');

    cy.setFieldSelectToLastOfEntity('invoice');

    cy.setFieldSelectToLastOfEntity('offer');

    cy.setFieldSelectToLastOfEntity('orderConfirmation');

    cy.get(entityCreateSaveButtonSelector).click({ force: true });
    cy.scrollTo('top', { ensureScrollable: false });
    cy.get(entityCreateSaveButtonSelector).should('not.exist');
    cy.wait('@postEntityRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(201);
    });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', descriptiveDocumentTextPageUrlPattern);
  });

  it('should delete last instance of DescriptiveDocumentText', function () {
    cy.visit(descriptiveDocumentTextPageUrl);
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length > 0) {
        cy.get(entityTableSelector).should('have.lengthOf', response.body.length);
        cy.get(entityDeleteButtonSelector).last().click({ force: true });
        cy.getEntityDeleteDialogHeading('descriptiveDocumentText').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', descriptiveDocumentTextPageUrlPattern);
      } else {
        this.skip();
      }
    });
  });
});
