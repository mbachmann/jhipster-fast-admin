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

describe('WorkingHour e2e test', () => {
  const workingHourPageUrl = '/working-hour';
  const workingHourPageUrlPattern = new RegExp('/working-hour(\\?.*)?$');
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
    cy.intercept('GET', '/api/working-hours+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/working-hours').as('postEntityRequest');
    cy.intercept('DELETE', '/api/working-hours/*').as('deleteEntityRequest');
  });

  it('should load WorkingHours', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('working-hour');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('WorkingHour').should('exist');
    cy.url().should('match', workingHourPageUrlPattern);
  });

  it('should load details WorkingHour page', function () {
    cy.visit(workingHourPageUrl);
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        this.skip();
      }
    });
    cy.get(entityDetailsButtonSelector).first().click({ force: true });
    cy.getEntityDetailsHeading('workingHour');
    cy.get(entityDetailsBackButtonSelector).click({ force: true });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', workingHourPageUrlPattern);
  });

  it('should load create WorkingHour page', () => {
    cy.visit(workingHourPageUrl);
    cy.wait('@entitiesRequest');
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('WorkingHour');
    cy.get(entityCreateSaveButtonSelector).should('exist');
    cy.get(entityCreateCancelButtonSelector).click({ force: true });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', workingHourPageUrlPattern);
  });

  it('should load edit WorkingHour page', function () {
    cy.visit(workingHourPageUrl);
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        this.skip();
      }
    });
    cy.get(entityEditButtonSelector).first().click({ force: true });
    cy.getEntityCreateUpdateHeading('WorkingHour');
    cy.get(entityCreateSaveButtonSelector).should('exist');
    cy.get(entityCreateCancelButtonSelector).click({ force: true });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', workingHourPageUrlPattern);
  });

  it('should create an instance of WorkingHour', () => {
    cy.visit(workingHourPageUrl);
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('WorkingHour');

    cy.get(`[data-cy="remoteId"]`).type('93209').should('have.value', '93209');

    cy.get(`[data-cy="userName"]`).type('Spur Jamaica optimize').should('have.value', 'Spur Jamaica optimize');

    cy.get(`[data-cy="date"]`).type('2021-08-16').should('have.value', '2021-08-16');

    cy.get(`[data-cy="timeStart"]`).type('2021-08-16T21:09').should('have.value', '2021-08-16T21:09');

    cy.get(`[data-cy="timeEnd"]`).type('2021-08-17T16:08').should('have.value', '2021-08-17T16:08');

    cy.get(`[data-cy="created"]`).type('2021-08-16T18:20').should('have.value', '2021-08-16T18:20');

    cy.setFieldSelectToLastOfEntity('applicationUser');

    cy.setFieldSelectToLastOfEntity('effort');

    cy.get(entityCreateSaveButtonSelector).click({ force: true });
    cy.scrollTo('top', { ensureScrollable: false });
    cy.get(entityCreateSaveButtonSelector).should('not.exist');
    cy.wait('@postEntityRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(201);
    });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', workingHourPageUrlPattern);
  });

  it('should delete last instance of WorkingHour', function () {
    cy.visit(workingHourPageUrl);
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length > 0) {
        cy.get(entityTableSelector).should('have.lengthOf', response.body.length);
        cy.get(entityDeleteButtonSelector).last().click({ force: true });
        cy.getEntityDeleteDialogHeading('workingHour').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', workingHourPageUrlPattern);
      } else {
        this.skip();
      }
    });
  });
});
