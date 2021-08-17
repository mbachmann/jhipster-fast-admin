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

describe('ContactAccount e2e test', () => {
  const contactAccountPageUrl = '/contact-account';
  const contactAccountPageUrlPattern = new RegExp('/contact-account(\\?.*)?$');
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
    cy.intercept('GET', '/api/contact-accounts+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/contact-accounts').as('postEntityRequest');
    cy.intercept('DELETE', '/api/contact-accounts/*').as('deleteEntityRequest');
  });

  it('should load ContactAccounts', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('contact-account');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('ContactAccount').should('exist');
    cy.url().should('match', contactAccountPageUrlPattern);
  });

  it('should load details ContactAccount page', function () {
    cy.visit(contactAccountPageUrl);
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        this.skip();
      }
    });
    cy.get(entityDetailsButtonSelector).first().click({ force: true });
    cy.getEntityDetailsHeading('contactAccount');
    cy.get(entityDetailsBackButtonSelector).click({ force: true });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', contactAccountPageUrlPattern);
  });

  it('should load create ContactAccount page', () => {
    cy.visit(contactAccountPageUrl);
    cy.wait('@entitiesRequest');
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('ContactAccount');
    cy.get(entityCreateSaveButtonSelector).should('exist');
    cy.get(entityCreateCancelButtonSelector).click({ force: true });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', contactAccountPageUrlPattern);
  });

  it('should load edit ContactAccount page', function () {
    cy.visit(contactAccountPageUrl);
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        this.skip();
      }
    });
    cy.get(entityEditButtonSelector).first().click({ force: true });
    cy.getEntityCreateUpdateHeading('ContactAccount');
    cy.get(entityCreateSaveButtonSelector).should('exist');
    cy.get(entityCreateCancelButtonSelector).click({ force: true });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', contactAccountPageUrlPattern);
  });

  it('should create an instance of ContactAccount', () => {
    cy.visit(contactAccountPageUrl);
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('ContactAccount');

    cy.get(`[data-cy="remoteId"]`).type('3936').should('have.value', '3936');

    cy.get(`[data-cy="defaultAccount"]`).should('not.be.checked');
    cy.get(`[data-cy="defaultAccount"]`).click().should('be.checked');

    cy.get(`[data-cy="type"]`).select('IBAN_NUMBER');

    cy.get(`[data-cy="number"]`).type('National driver Dynamic').should('have.value', 'National driver Dynamic');

    cy.get(`[data-cy="bic"]`).type('CBEEBBZ1').should('have.value', 'CBEEBBZ1');

    cy.get(`[data-cy="description"]`).type('teal generation Mall').should('have.value', 'teal generation Mall');

    cy.get(`[data-cy="inactiv"]`).should('not.be.checked');
    cy.get(`[data-cy="inactiv"]`).click().should('be.checked');

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
    cy.url().should('match', contactAccountPageUrlPattern);
  });

  it('should delete last instance of ContactAccount', function () {
    cy.visit(contactAccountPageUrl);
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length > 0) {
        cy.get(entityTableSelector).should('have.lengthOf', response.body.length);
        cy.get(entityDeleteButtonSelector).last().click({ force: true });
        cy.getEntityDeleteDialogHeading('contactAccount').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', contactAccountPageUrlPattern);
      } else {
        this.skip();
      }
    });
  });
});
