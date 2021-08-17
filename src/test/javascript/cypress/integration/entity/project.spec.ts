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

describe('Project e2e test', () => {
  const projectPageUrl = '/project';
  const projectPageUrlPattern = new RegExp('/project(\\?.*)?$');
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
    cy.intercept('GET', '/api/projects+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/projects').as('postEntityRequest');
    cy.intercept('DELETE', '/api/projects/*').as('deleteEntityRequest');
  });

  it('should load Projects', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('project');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Project').should('exist');
    cy.url().should('match', projectPageUrlPattern);
  });

  it('should load details Project page', function () {
    cy.visit(projectPageUrl);
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        this.skip();
      }
    });
    cy.get(entityDetailsButtonSelector).first().click({ force: true });
    cy.getEntityDetailsHeading('project');
    cy.get(entityDetailsBackButtonSelector).click({ force: true });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', projectPageUrlPattern);
  });

  it('should load create Project page', () => {
    cy.visit(projectPageUrl);
    cy.wait('@entitiesRequest');
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('Project');
    cy.get(entityCreateSaveButtonSelector).should('exist');
    cy.get(entityCreateCancelButtonSelector).click({ force: true });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', projectPageUrlPattern);
  });

  it('should load edit Project page', function () {
    cy.visit(projectPageUrl);
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        this.skip();
      }
    });
    cy.get(entityEditButtonSelector).first().click({ force: true });
    cy.getEntityCreateUpdateHeading('Project');
    cy.get(entityCreateSaveButtonSelector).should('exist');
    cy.get(entityCreateCancelButtonSelector).click({ force: true });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', projectPageUrlPattern);
  });

  it('should create an instance of Project', () => {
    cy.visit(projectPageUrl);
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('Project');

    cy.get(`[data-cy="remoteId"]`).type('29073').should('have.value', '29073');

    cy.get(`[data-cy="number"]`).type('Riyal Fantastic brand').should('have.value', 'Riyal Fantastic brand');

    cy.get(`[data-cy="contactName"]`).type('blue French').should('have.value', 'blue French');

    cy.get(`[data-cy="name"]`).type('virtual Checking UIC-Franc').should('have.value', 'virtual Checking UIC-Franc');

    cy.get(`[data-cy="description"]`).type('online').should('have.value', 'online');

    cy.get(`[data-cy="startDate"]`).type('2021-08-17').should('have.value', '2021-08-17');

    cy.get(`[data-cy="hoursEstimated"]`).type('76078').should('have.value', '76078');

    cy.get(`[data-cy="hourlyRate"]`).type('61219').should('have.value', '61219');

    cy.get(`[data-cy="status"]`).select('PENDING');

    cy.setFieldSelectToLastOfEntity('contact');

    cy.get(entityCreateSaveButtonSelector).click({ force: true });
    cy.scrollTo('top', { ensureScrollable: false });
    cy.get(entityCreateSaveButtonSelector).should('not.exist');
    cy.wait('@postEntityRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(201);
    });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', projectPageUrlPattern);
  });

  it('should delete last instance of Project', function () {
    cy.visit(projectPageUrl);
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length > 0) {
        cy.get(entityTableSelector).should('have.lengthOf', response.body.length);
        cy.get(entityDeleteButtonSelector).last().click({ force: true });
        cy.getEntityDeleteDialogHeading('project').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', projectPageUrlPattern);
      } else {
        this.skip();
      }
    });
  });
});
