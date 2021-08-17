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

describe('ExchangeRate e2e test', () => {
  const exchangeRatePageUrl = '/exchange-rate';
  const exchangeRatePageUrlPattern = new RegExp('/exchange-rate(\\?.*)?$');
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
    cy.intercept('GET', '/api/exchange-rates+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/exchange-rates').as('postEntityRequest');
    cy.intercept('DELETE', '/api/exchange-rates/*').as('deleteEntityRequest');
  });

  it('should load ExchangeRates', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('exchange-rate');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('ExchangeRate').should('exist');
    cy.url().should('match', exchangeRatePageUrlPattern);
  });

  it('should load details ExchangeRate page', function () {
    cy.visit(exchangeRatePageUrl);
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        this.skip();
      }
    });
    cy.get(entityDetailsButtonSelector).first().click({ force: true });
    cy.getEntityDetailsHeading('exchangeRate');
    cy.get(entityDetailsBackButtonSelector).click({ force: true });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', exchangeRatePageUrlPattern);
  });

  it('should load create ExchangeRate page', () => {
    cy.visit(exchangeRatePageUrl);
    cy.wait('@entitiesRequest');
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('ExchangeRate');
    cy.get(entityCreateSaveButtonSelector).should('exist');
    cy.get(entityCreateCancelButtonSelector).click({ force: true });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', exchangeRatePageUrlPattern);
  });

  it('should load edit ExchangeRate page', function () {
    cy.visit(exchangeRatePageUrl);
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        this.skip();
      }
    });
    cy.get(entityEditButtonSelector).first().click({ force: true });
    cy.getEntityCreateUpdateHeading('ExchangeRate');
    cy.get(entityCreateSaveButtonSelector).should('exist');
    cy.get(entityCreateCancelButtonSelector).click({ force: true });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', exchangeRatePageUrlPattern);
  });

  it('should create an instance of ExchangeRate', () => {
    cy.visit(exchangeRatePageUrl);
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('ExchangeRate');

    cy.get(`[data-cy="remoteId"]`).type('36493').should('have.value', '36493');

    cy.get(`[data-cy="currencyFrom"]`).select('SRD');

    cy.get(`[data-cy="currencyTo"]`).select('XTS');

    cy.get(`[data-cy="rate"]`).type('64438').should('have.value', '64438');

    cy.get(`[data-cy="created"]`).type('2021-08-17T10:24').should('have.value', '2021-08-17T10:24');

    cy.get(`[data-cy="inactiv"]`).should('not.be.checked');
    cy.get(`[data-cy="inactiv"]`).click().should('be.checked');

    cy.get(entityCreateSaveButtonSelector).click({ force: true });
    cy.scrollTo('top', { ensureScrollable: false });
    cy.get(entityCreateSaveButtonSelector).should('not.exist');
    cy.wait('@postEntityRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(201);
    });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', exchangeRatePageUrlPattern);
  });

  it('should delete last instance of ExchangeRate', function () {
    cy.visit(exchangeRatePageUrl);
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length > 0) {
        cy.get(entityTableSelector).should('have.lengthOf', response.body.length);
        cy.get(entityDeleteButtonSelector).last().click({ force: true });
        cy.getEntityDeleteDialogHeading('exchangeRate').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', exchangeRatePageUrlPattern);
      } else {
        this.skip();
      }
    });
  });
});
