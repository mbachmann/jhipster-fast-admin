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

describe('Effort e2e test', () => {
  const effortPageUrl = '/effort-fa';
  const effortPageUrlPattern = new RegExp('/effort-fa(\\?.*)?$');
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
    cy.intercept('GET', '/api/efforts+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/efforts').as('postEntityRequest');
    cy.intercept('DELETE', '/api/efforts/*').as('deleteEntityRequest');
  });

  it('should load Efforts', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('effort-fa');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Effort').should('exist');
    cy.url().should('match', effortPageUrlPattern);
  });

  it('should load details Effort page', function () {
    cy.visit(effortPageUrl);
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        this.skip();
      }
    });
    cy.get(entityDetailsButtonSelector).first().click({ force: true });
    cy.getEntityDetailsHeading('effort');
    cy.get(entityDetailsBackButtonSelector).click({ force: true });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', effortPageUrlPattern);
  });

  it('should load create Effort page', () => {
    cy.visit(effortPageUrl);
    cy.wait('@entitiesRequest');
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('Effort');
    cy.get(entityCreateSaveButtonSelector).should('exist');
    cy.get(entityCreateCancelButtonSelector).click({ force: true });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', effortPageUrlPattern);
  });

  it('should load edit Effort page', function () {
    cy.visit(effortPageUrl);
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        this.skip();
      }
    });
    cy.get(entityEditButtonSelector).first().click({ force: true });
    cy.getEntityCreateUpdateHeading('Effort');
    cy.get(entityCreateSaveButtonSelector).should('exist');
    cy.get(entityCreateCancelButtonSelector).click({ force: true });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', effortPageUrlPattern);
  });

  it('should create an instance of Effort', () => {
    cy.visit(effortPageUrl);
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('Effort');

    cy.get(`[data-cy="remoteId"]`).type('35864').should('have.value', '35864');

    cy.get(`[data-cy="userId"]`).type('57904').should('have.value', '57904');

    cy.get(`[data-cy="userName"]`).type('transmitter Clothing').should('have.value', 'transmitter Clothing');

    cy.get(`[data-cy="entityType"]`).select('COST_UNIT');

    cy.get(`[data-cy="entityId"]`).type('69710').should('have.value', '69710');

    cy.get(`[data-cy="duration"]`).type('54846').should('have.value', '54846');

    cy.get(`[data-cy="date"]`).type('2021-08-17').should('have.value', '2021-08-17');

    cy.get(`[data-cy="activityName"]`).type('deposit Grocery').should('have.value', 'deposit Grocery');

    cy.get(`[data-cy="notes"]`).type('Pennsylvania overriding Afghanistan').should('have.value', 'Pennsylvania overriding Afghanistan');

    cy.get(`[data-cy="isInvoiced"]`).should('not.be.checked');
    cy.get(`[data-cy="isInvoiced"]`).click().should('be.checked');

    cy.get(`[data-cy="updated"]`).type('2021-08-16T14:04').should('have.value', '2021-08-16T14:04');

    cy.get(`[data-cy="hourlyRate"]`).type('80710').should('have.value', '80710');

    cy.setFieldSelectToLastOfEntity('activity');

    cy.get(entityCreateSaveButtonSelector).click({ force: true });
    cy.scrollTo('top', { ensureScrollable: false });
    cy.get(entityCreateSaveButtonSelector).should('not.exist');
    cy.wait('@postEntityRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(201);
    });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', effortPageUrlPattern);
  });

  it('should delete last instance of Effort', function () {
    cy.visit(effortPageUrl);
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length > 0) {
        cy.get(entityTableSelector).should('have.lengthOf', response.body.length);
        cy.get(entityDeleteButtonSelector).last().click({ force: true });
        cy.getEntityDeleteDialogHeading('effort').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', effortPageUrlPattern);
      } else {
        this.skip();
      }
    });
  });
});
