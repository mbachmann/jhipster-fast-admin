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

describe('ReportingActivity e2e test', () => {
  const reportingActivityPageUrl = '/reporting-activity';
  const reportingActivityPageUrlPattern = new RegExp('/reporting-activity(\\?.*)?$');
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
    cy.intercept('GET', '/api/reporting-activities+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/reporting-activities').as('postEntityRequest');
    cy.intercept('DELETE', '/api/reporting-activities/*').as('deleteEntityRequest');
  });

  it('should load ReportingActivities', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('reporting-activity');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('ReportingActivity').should('exist');
    cy.url().should('match', reportingActivityPageUrlPattern);
  });

  it('should load details ReportingActivity page', function () {
    cy.visit(reportingActivityPageUrl);
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        this.skip();
      }
    });
    cy.get(entityDetailsButtonSelector).first().click({ force: true });
    cy.getEntityDetailsHeading('reportingActivity');
    cy.get(entityDetailsBackButtonSelector).click({ force: true });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', reportingActivityPageUrlPattern);
  });

  it('should load create ReportingActivity page', () => {
    cy.visit(reportingActivityPageUrl);
    cy.wait('@entitiesRequest');
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('ReportingActivity');
    cy.get(entityCreateSaveButtonSelector).should('exist');
    cy.get(entityCreateCancelButtonSelector).click({ force: true });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', reportingActivityPageUrlPattern);
  });

  it('should load edit ReportingActivity page', function () {
    cy.visit(reportingActivityPageUrl);
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        this.skip();
      }
    });
    cy.get(entityEditButtonSelector).first().click({ force: true });
    cy.getEntityCreateUpdateHeading('ReportingActivity');
    cy.get(entityCreateSaveButtonSelector).should('exist');
    cy.get(entityCreateCancelButtonSelector).click({ force: true });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', reportingActivityPageUrlPattern);
  });

  it('should create an instance of ReportingActivity', () => {
    cy.visit(reportingActivityPageUrl);
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('ReportingActivity');

    cy.get(`[data-cy="remoteId"]`).type('96255').should('have.value', '96255');

    cy.get(`[data-cy="name"]`).type('Avon Frozen').should('have.value', 'Avon Frozen');

    cy.get(`[data-cy="useServicePrice"]`).should('not.be.checked');
    cy.get(`[data-cy="useServicePrice"]`).click().should('be.checked');

    cy.get(`[data-cy="inactiv"]`).should('not.be.checked');
    cy.get(`[data-cy="inactiv"]`).click().should('be.checked');

    cy.setFieldSelectToLastOfEntity('catalogService');

    cy.get(entityCreateSaveButtonSelector).click({ force: true });
    cy.scrollTo('top', { ensureScrollable: false });
    cy.get(entityCreateSaveButtonSelector).should('not.exist');
    cy.wait('@postEntityRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(201);
    });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', reportingActivityPageUrlPattern);
  });

  it('should delete last instance of ReportingActivity', function () {
    cy.visit(reportingActivityPageUrl);
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length > 0) {
        cy.get(entityTableSelector).should('have.lengthOf', response.body.length);
        cy.get(entityDeleteButtonSelector).last().click({ force: true });
        cy.getEntityDeleteDialogHeading('reportingActivity').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', reportingActivityPageUrlPattern);
      } else {
        this.skip();
      }
    });
  });
});
