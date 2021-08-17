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

describe('DocumentFreeText e2e test', () => {
  const documentFreeTextPageUrl = '/document-free-text-fa';
  const documentFreeTextPageUrlPattern = new RegExp('/document-free-text-fa(\\?.*)?$');
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
    cy.intercept('GET', '/api/document-free-texts+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/document-free-texts').as('postEntityRequest');
    cy.intercept('DELETE', '/api/document-free-texts/*').as('deleteEntityRequest');
  });

  it('should load DocumentFreeTexts', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('document-free-text-fa');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('DocumentFreeText').should('exist');
    cy.url().should('match', documentFreeTextPageUrlPattern);
  });

  it('should load details DocumentFreeText page', function () {
    cy.visit(documentFreeTextPageUrl);
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        this.skip();
      }
    });
    cy.get(entityDetailsButtonSelector).first().click({ force: true });
    cy.getEntityDetailsHeading('documentFreeText');
    cy.get(entityDetailsBackButtonSelector).click({ force: true });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', documentFreeTextPageUrlPattern);
  });

  it('should load create DocumentFreeText page', () => {
    cy.visit(documentFreeTextPageUrl);
    cy.wait('@entitiesRequest');
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('DocumentFreeText');
    cy.get(entityCreateSaveButtonSelector).should('exist');
    cy.get(entityCreateCancelButtonSelector).click({ force: true });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', documentFreeTextPageUrlPattern);
  });

  it('should load edit DocumentFreeText page', function () {
    cy.visit(documentFreeTextPageUrl);
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        this.skip();
      }
    });
    cy.get(entityEditButtonSelector).first().click({ force: true });
    cy.getEntityCreateUpdateHeading('DocumentFreeText');
    cy.get(entityCreateSaveButtonSelector).should('exist');
    cy.get(entityCreateCancelButtonSelector).click({ force: true });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', documentFreeTextPageUrlPattern);
  });

  it('should create an instance of DocumentFreeText', () => {
    cy.visit(documentFreeTextPageUrl);
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('DocumentFreeText');

    cy.get(`[data-cy="text"]`).type('Bike maroon').should('have.value', 'Bike maroon');

    cy.get(`[data-cy="fontSize"]`).type('18924').should('have.value', '18924');

    cy.get(`[data-cy="positionX"]`).type('66856').should('have.value', '66856');

    cy.get(`[data-cy="positionY"]`).type('14537').should('have.value', '14537');

    cy.get(`[data-cy="pageNo"]`).type('16719').should('have.value', '16719');

    cy.setFieldSelectToLastOfEntity('documentLetter');

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
    cy.url().should('match', documentFreeTextPageUrlPattern);
  });

  it('should delete last instance of DocumentFreeText', function () {
    cy.visit(documentFreeTextPageUrl);
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length > 0) {
        cy.get(entityTableSelector).should('have.lengthOf', response.body.length);
        cy.get(entityDeleteButtonSelector).last().click({ force: true });
        cy.getEntityDeleteDialogHeading('documentFreeText').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', documentFreeTextPageUrlPattern);
      } else {
        this.skip();
      }
    });
  });
});
