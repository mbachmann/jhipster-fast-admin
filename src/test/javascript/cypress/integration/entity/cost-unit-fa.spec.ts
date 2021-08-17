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

describe('CostUnit e2e test', () => {
  const costUnitPageUrl = '/cost-unit-fa';
  const costUnitPageUrlPattern = new RegExp('/cost-unit-fa(\\?.*)?$');
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
    cy.intercept('GET', '/api/cost-units+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/cost-units').as('postEntityRequest');
    cy.intercept('DELETE', '/api/cost-units/*').as('deleteEntityRequest');
  });

  it('should load CostUnits', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('cost-unit-fa');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('CostUnit').should('exist');
    cy.url().should('match', costUnitPageUrlPattern);
  });

  it('should load details CostUnit page', function () {
    cy.visit(costUnitPageUrl);
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        this.skip();
      }
    });
    cy.get(entityDetailsButtonSelector).first().click({ force: true });
    cy.getEntityDetailsHeading('costUnit');
    cy.get(entityDetailsBackButtonSelector).click({ force: true });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', costUnitPageUrlPattern);
  });

  it('should load create CostUnit page', () => {
    cy.visit(costUnitPageUrl);
    cy.wait('@entitiesRequest');
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('CostUnit');
    cy.get(entityCreateSaveButtonSelector).should('exist');
    cy.get(entityCreateCancelButtonSelector).click({ force: true });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', costUnitPageUrlPattern);
  });

  it('should load edit CostUnit page', function () {
    cy.visit(costUnitPageUrl);
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        this.skip();
      }
    });
    cy.get(entityEditButtonSelector).first().click({ force: true });
    cy.getEntityCreateUpdateHeading('CostUnit');
    cy.get(entityCreateSaveButtonSelector).should('exist');
    cy.get(entityCreateCancelButtonSelector).click({ force: true });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', costUnitPageUrlPattern);
  });

  it('should create an instance of CostUnit', () => {
    cy.visit(costUnitPageUrl);
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('CostUnit');

    cy.get(`[data-cy="remoteId"]`).type('85607').should('have.value', '85607');

    cy.get(`[data-cy="number"]`).type('Cliffs navigating vertical').should('have.value', 'Cliffs navigating vertical');

    cy.get(`[data-cy="name"]`).type('collaborative Carolina').should('have.value', 'collaborative Carolina');

    cy.get(`[data-cy="description"]`).type('Division').should('have.value', 'Division');

    cy.get(`[data-cy="type"]`).select('NOT_PRODUCTIVE');

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
    cy.url().should('match', costUnitPageUrlPattern);
  });

  it('should delete last instance of CostUnit', function () {
    cy.visit(costUnitPageUrl);
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length > 0) {
        cy.get(entityTableSelector).should('have.lengthOf', response.body.length);
        cy.get(entityDeleteButtonSelector).last().click({ force: true });
        cy.getEntityDeleteDialogHeading('costUnit').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', costUnitPageUrlPattern);
      } else {
        this.skip();
      }
    });
  });
});
