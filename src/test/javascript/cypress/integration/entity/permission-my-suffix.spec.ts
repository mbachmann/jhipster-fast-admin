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

describe('Permission e2e test', () => {
  const permissionPageUrl = '/permission-my-suffix';
  const permissionPageUrlPattern = new RegExp('/permission-my-suffix(\\?.*)?$');
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
    cy.intercept('GET', '/api/permissions+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/permissions').as('postEntityRequest');
    cy.intercept('DELETE', '/api/permissions/*').as('deleteEntityRequest');
  });

  it('should load Permissions', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('permission-my-suffix');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Permission').should('exist');
    cy.url().should('match', permissionPageUrlPattern);
  });

  it('should load details Permission page', function () {
    cy.visit(permissionPageUrl);
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        this.skip();
      }
    });
    cy.get(entityDetailsButtonSelector).first().click({ force: true });
    cy.getEntityDetailsHeading('permission');
    cy.get(entityDetailsBackButtonSelector).click({ force: true });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', permissionPageUrlPattern);
  });

  it('should load create Permission page', () => {
    cy.visit(permissionPageUrl);
    cy.wait('@entitiesRequest');
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('Permission');
    cy.get(entityCreateSaveButtonSelector).should('exist');
    cy.get(entityCreateCancelButtonSelector).click({ force: true });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', permissionPageUrlPattern);
  });

  it('should load edit Permission page', function () {
    cy.visit(permissionPageUrl);
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        this.skip();
      }
    });
    cy.get(entityEditButtonSelector).first().click({ force: true });
    cy.getEntityCreateUpdateHeading('Permission');
    cy.get(entityCreateSaveButtonSelector).should('exist');
    cy.get(entityCreateCancelButtonSelector).click({ force: true });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', permissionPageUrlPattern);
  });

  it('should create an instance of Permission', () => {
    cy.visit(permissionPageUrl);
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('Permission');

    cy.get(`[data-cy="newAll"]`).should('not.be.checked');
    cy.get(`[data-cy="newAll"]`).click().should('be.checked');

    cy.get(`[data-cy="editOwn"]`).should('not.be.checked');
    cy.get(`[data-cy="editOwn"]`).click().should('be.checked');

    cy.get(`[data-cy="editAll"]`).should('not.be.checked');
    cy.get(`[data-cy="editAll"]`).click().should('be.checked');

    cy.get(`[data-cy="viewOwn"]`).should('not.be.checked');
    cy.get(`[data-cy="viewOwn"]`).click().should('be.checked');

    cy.get(`[data-cy="viewAll"]`).should('not.be.checked');
    cy.get(`[data-cy="viewAll"]`).click().should('be.checked');

    cy.get(`[data-cy="manageOwn"]`).should('not.be.checked');
    cy.get(`[data-cy="manageOwn"]`).click().should('be.checked');

    cy.get(`[data-cy="manageAll"]`).should('not.be.checked');
    cy.get(`[data-cy="manageAll"]`).click().should('be.checked');

    cy.get(`[data-cy="domainResource"]`).select('OFFERS');

    cy.setFieldSelectToLastOfEntity('role');

    cy.setFieldSelectToLastOfEntity('contact');

    cy.setFieldSelectToLastOfEntity('contactAddress');

    cy.get(entityCreateSaveButtonSelector).click({ force: true });
    cy.scrollTo('top', { ensureScrollable: false });
    cy.get(entityCreateSaveButtonSelector).should('not.exist');
    cy.wait('@postEntityRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(201);
    });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', permissionPageUrlPattern);
  });

  it('should delete last instance of Permission', function () {
    cy.visit(permissionPageUrl);
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length > 0) {
        cy.get(entityTableSelector).should('have.lengthOf', response.body.length);
        cy.get(entityDeleteButtonSelector).last().click({ force: true });
        cy.getEntityDeleteDialogHeading('permission').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', permissionPageUrlPattern);
      } else {
        this.skip();
      }
    });
  });
});
