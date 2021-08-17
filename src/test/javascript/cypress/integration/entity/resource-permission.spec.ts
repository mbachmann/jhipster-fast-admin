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

describe('ResourcePermission e2e test', () => {
  const resourcePermissionPageUrl = '/resource-permission';
  const resourcePermissionPageUrlPattern = new RegExp('/resource-permission(\\?.*)?$');
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
    cy.intercept('GET', '/api/resource-permissions+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/resource-permissions').as('postEntityRequest');
    cy.intercept('DELETE', '/api/resource-permissions/*').as('deleteEntityRequest');
  });

  it('should load ResourcePermissions', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('resource-permission');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('ResourcePermission').should('exist');
    cy.url().should('match', resourcePermissionPageUrlPattern);
  });

  it('should load details ResourcePermission page', function () {
    cy.visit(resourcePermissionPageUrl);
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        this.skip();
      }
    });
    cy.get(entityDetailsButtonSelector).first().click({ force: true });
    cy.getEntityDetailsHeading('resourcePermission');
    cy.get(entityDetailsBackButtonSelector).click({ force: true });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', resourcePermissionPageUrlPattern);
  });

  it('should load create ResourcePermission page', () => {
    cy.visit(resourcePermissionPageUrl);
    cy.wait('@entitiesRequest');
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('ResourcePermission');
    cy.get(entityCreateSaveButtonSelector).should('exist');
    cy.get(entityCreateCancelButtonSelector).click({ force: true });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', resourcePermissionPageUrlPattern);
  });

  it('should load edit ResourcePermission page', function () {
    cy.visit(resourcePermissionPageUrl);
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        this.skip();
      }
    });
    cy.get(entityEditButtonSelector).first().click({ force: true });
    cy.getEntityCreateUpdateHeading('ResourcePermission');
    cy.get(entityCreateSaveButtonSelector).should('exist');
    cy.get(entityCreateCancelButtonSelector).click({ force: true });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', resourcePermissionPageUrlPattern);
  });

  it('should create an instance of ResourcePermission', () => {
    cy.visit(resourcePermissionPageUrl);
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('ResourcePermission');

    cy.get(`[data-cy="add"]`).select('ALL');

    cy.get(`[data-cy="edit"]`).select('OWN');

    cy.get(`[data-cy="manage"]`).select('NONE');

    cy.get(`[data-cy="domainArea"]`).select('CONTACTS');

    cy.setFieldSelectToLastOfEntity('role');

    cy.setFieldSelectToLastOfEntity('applicationUser');

    cy.get(entityCreateSaveButtonSelector).click({ force: true });
    cy.scrollTo('top', { ensureScrollable: false });
    cy.get(entityCreateSaveButtonSelector).should('not.exist');
    cy.wait('@postEntityRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(201);
    });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', resourcePermissionPageUrlPattern);
  });

  it('should delete last instance of ResourcePermission', function () {
    cy.visit(resourcePermissionPageUrl);
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length > 0) {
        cy.get(entityTableSelector).should('have.lengthOf', response.body.length);
        cy.get(entityDeleteButtonSelector).last().click({ force: true });
        cy.getEntityDeleteDialogHeading('resourcePermission').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', resourcePermissionPageUrlPattern);
      } else {
        this.skip();
      }
    });
  });
});
