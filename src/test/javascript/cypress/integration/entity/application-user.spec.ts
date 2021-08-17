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

describe('ApplicationUser e2e test', () => {
  const applicationUserPageUrl = '/application-user';
  const applicationUserPageUrlPattern = new RegExp('/application-user(\\?.*)?$');
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
    cy.intercept('GET', '/api/application-users+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/application-users').as('postEntityRequest');
    cy.intercept('DELETE', '/api/application-users/*').as('deleteEntityRequest');
  });

  it('should load ApplicationUsers', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('application-user');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('ApplicationUser').should('exist');
    cy.url().should('match', applicationUserPageUrlPattern);
  });

  it('should load details ApplicationUser page', function () {
    cy.visit(applicationUserPageUrl);
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        this.skip();
      }
    });
    cy.get(entityDetailsButtonSelector).first().click({ force: true });
    cy.getEntityDetailsHeading('applicationUser');
    cy.get(entityDetailsBackButtonSelector).click({ force: true });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', applicationUserPageUrlPattern);
  });

  it('should load create ApplicationUser page', () => {
    cy.visit(applicationUserPageUrl);
    cy.wait('@entitiesRequest');
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('ApplicationUser');
    cy.get(entityCreateSaveButtonSelector).should('exist');
    cy.get(entityCreateCancelButtonSelector).click({ force: true });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', applicationUserPageUrlPattern);
  });

  it('should load edit ApplicationUser page', function () {
    cy.visit(applicationUserPageUrl);
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        this.skip();
      }
    });
    cy.get(entityEditButtonSelector).first().click({ force: true });
    cy.getEntityCreateUpdateHeading('ApplicationUser');
    cy.get(entityCreateSaveButtonSelector).should('exist');
    cy.get(entityCreateCancelButtonSelector).click({ force: true });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', applicationUserPageUrlPattern);
  });

  it('should create an instance of ApplicationUser', () => {
    cy.visit(applicationUserPageUrl);
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('ApplicationUser');

    cy.get(`[data-cy="shortCutName"]`).type('Isle Keyboard Account').should('have.value', 'Isle Keyboard Account');

    cy.get(`[data-cy="memberSince"]`).type('2021-08-17').should('have.value', '2021-08-17');

    cy.setFieldImageAsBytesOfEntity('avatar', 'integration-test.png', 'image/png');

    cy.get(`[data-cy="imageType"]`).type('payment Tennessee Shoes').should('have.value', 'payment Tennessee Shoes');

    cy.get(`[data-cy="inactiv"]`).should('not.be.checked');
    cy.get(`[data-cy="inactiv"]`).click().should('be.checked');

    // since cypress clicks submit too fast before the blob fields are validated
    cy.wait(200); // eslint-disable-line cypress/no-unnecessary-waiting
    cy.get(entityCreateSaveButtonSelector).click({ force: true });
    cy.scrollTo('top', { ensureScrollable: false });
    cy.get(entityCreateSaveButtonSelector).should('not.exist');
    cy.wait('@postEntityRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(201);
    });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', applicationUserPageUrlPattern);
  });

  it('should delete last instance of ApplicationUser', function () {
    cy.visit(applicationUserPageUrl);
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length > 0) {
        cy.get(entityTableSelector).should('have.lengthOf', response.body.length);
        cy.get(entityDeleteButtonSelector).last().click({ force: true });
        cy.getEntityDeleteDialogHeading('applicationUser').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', applicationUserPageUrlPattern);
      } else {
        this.skip();
      }
    });
  });
});
