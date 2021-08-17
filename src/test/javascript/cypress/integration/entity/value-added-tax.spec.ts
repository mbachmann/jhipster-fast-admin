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

describe('ValueAddedTax e2e test', () => {
  const valueAddedTaxPageUrl = '/value-added-tax';
  const valueAddedTaxPageUrlPattern = new RegExp('/value-added-tax(\\?.*)?$');
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
    cy.intercept('GET', '/api/value-added-taxes+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/value-added-taxes').as('postEntityRequest');
    cy.intercept('DELETE', '/api/value-added-taxes/*').as('deleteEntityRequest');
  });

  it('should load ValueAddedTaxes', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('value-added-tax');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('ValueAddedTax').should('exist');
    cy.url().should('match', valueAddedTaxPageUrlPattern);
  });

  it('should load details ValueAddedTax page', function () {
    cy.visit(valueAddedTaxPageUrl);
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        this.skip();
      }
    });
    cy.get(entityDetailsButtonSelector).first().click({ force: true });
    cy.getEntityDetailsHeading('valueAddedTax');
    cy.get(entityDetailsBackButtonSelector).click({ force: true });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', valueAddedTaxPageUrlPattern);
  });

  it('should load create ValueAddedTax page', () => {
    cy.visit(valueAddedTaxPageUrl);
    cy.wait('@entitiesRequest');
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('ValueAddedTax');
    cy.get(entityCreateSaveButtonSelector).should('exist');
    cy.get(entityCreateCancelButtonSelector).click({ force: true });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', valueAddedTaxPageUrlPattern);
  });

  it('should load edit ValueAddedTax page', function () {
    cy.visit(valueAddedTaxPageUrl);
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        this.skip();
      }
    });
    cy.get(entityEditButtonSelector).first().click({ force: true });
    cy.getEntityCreateUpdateHeading('ValueAddedTax');
    cy.get(entityCreateSaveButtonSelector).should('exist');
    cy.get(entityCreateCancelButtonSelector).click({ force: true });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', valueAddedTaxPageUrlPattern);
  });

  it('should create an instance of ValueAddedTax', () => {
    cy.visit(valueAddedTaxPageUrl);
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('ValueAddedTax');

    cy.get(`[data-cy="name"]`).type('back transmit').should('have.value', 'back transmit');

    cy.get(`[data-cy="vatType"]`).select('PERCENT');

    cy.get(`[data-cy="validFrom"]`).type('2021-08-17').should('have.value', '2021-08-17');

    cy.get(`[data-cy="validUntil"]`).type('2021-08-16').should('have.value', '2021-08-16');

    cy.get(`[data-cy="vatPercent"]`).type('8182').should('have.value', '8182');

    cy.get(`[data-cy="inactiv"]`).should('not.be.checked');
    cy.get(`[data-cy="inactiv"]`).click().should('be.checked');

    cy.get(`[data-cy="newVatId"]`).type('67938').should('have.value', '67938');

    cy.get(entityCreateSaveButtonSelector).click({ force: true });
    cy.scrollTo('top', { ensureScrollable: false });
    cy.get(entityCreateSaveButtonSelector).should('not.exist');
    cy.wait('@postEntityRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(201);
    });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', valueAddedTaxPageUrlPattern);
  });

  it('should delete last instance of ValueAddedTax', function () {
    cy.visit(valueAddedTaxPageUrl);
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length > 0) {
        cy.get(entityTableSelector).should('have.lengthOf', response.body.length);
        cy.get(entityDeleteButtonSelector).last().click({ force: true });
        cy.getEntityDeleteDialogHeading('valueAddedTax').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', valueAddedTaxPageUrlPattern);
      } else {
        this.skip();
      }
    });
  });
});
