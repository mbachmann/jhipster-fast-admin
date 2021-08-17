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

describe('FreeText e2e test', () => {
  const freeTextPageUrl = '/free-text';
  const freeTextPageUrlPattern = new RegExp('/free-text(\\?.*)?$');
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
    cy.intercept('GET', '/api/free-texts+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/free-texts').as('postEntityRequest');
    cy.intercept('DELETE', '/api/free-texts/*').as('deleteEntityRequest');
  });

  it('should load FreeTexts', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('free-text');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('FreeText').should('exist');
    cy.url().should('match', freeTextPageUrlPattern);
  });

  it('should load details FreeText page', function () {
    cy.visit(freeTextPageUrl);
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        this.skip();
      }
    });
    cy.get(entityDetailsButtonSelector).first().click({ force: true });
    cy.getEntityDetailsHeading('freeText');
    cy.get(entityDetailsBackButtonSelector).click({ force: true });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', freeTextPageUrlPattern);
  });

  it('should load create FreeText page', () => {
    cy.visit(freeTextPageUrl);
    cy.wait('@entitiesRequest');
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('FreeText');
    cy.get(entityCreateSaveButtonSelector).should('exist');
    cy.get(entityCreateCancelButtonSelector).click({ force: true });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', freeTextPageUrlPattern);
  });

  it('should load edit FreeText page', function () {
    cy.visit(freeTextPageUrl);
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        this.skip();
      }
    });
    cy.get(entityEditButtonSelector).first().click({ force: true });
    cy.getEntityCreateUpdateHeading('FreeText');
    cy.get(entityCreateSaveButtonSelector).should('exist');
    cy.get(entityCreateCancelButtonSelector).click({ force: true });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', freeTextPageUrlPattern);
  });

  it('should create an instance of FreeText', () => {
    cy.visit(freeTextPageUrl);
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('FreeText');

    cy.get(`[data-cy="text"]`).type('Money Developer Human').should('have.value', 'Money Developer Human');

    cy.get(`[data-cy="fontSize"]`).type('15357').should('have.value', '15357');

    cy.get(`[data-cy="positionX"]`).type('93409').should('have.value', '93409');

    cy.get(`[data-cy="positionY"]`).type('96949').should('have.value', '96949');

    cy.get(`[data-cy="pageNo"]`).type('202').should('have.value', '202');

    cy.get(`[data-cy="language"]`).select('ITALIAN');

    cy.get(entityCreateSaveButtonSelector).click({ force: true });
    cy.scrollTo('top', { ensureScrollable: false });
    cy.get(entityCreateSaveButtonSelector).should('not.exist');
    cy.wait('@postEntityRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(201);
    });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', freeTextPageUrlPattern);
  });

  it('should delete last instance of FreeText', function () {
    cy.visit(freeTextPageUrl);
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length > 0) {
        cy.get(entityTableSelector).should('have.lengthOf', response.body.length);
        cy.get(entityDeleteButtonSelector).last().click({ force: true });
        cy.getEntityDeleteDialogHeading('freeText').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', freeTextPageUrlPattern);
      } else {
        this.skip();
      }
    });
  });
});
