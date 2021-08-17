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

describe('DocumentLetter e2e test', () => {
  const documentLetterPageUrl = '/document-letter';
  const documentLetterPageUrlPattern = new RegExp('/document-letter(\\?.*)?$');
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
    cy.intercept('GET', '/api/document-letters+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/document-letters').as('postEntityRequest');
    cy.intercept('DELETE', '/api/document-letters/*').as('deleteEntityRequest');
  });

  it('should load DocumentLetters', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('document-letter');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('DocumentLetter').should('exist');
    cy.url().should('match', documentLetterPageUrlPattern);
  });

  it('should load details DocumentLetter page', function () {
    cy.visit(documentLetterPageUrl);
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        this.skip();
      }
    });
    cy.get(entityDetailsButtonSelector).first().click({ force: true });
    cy.getEntityDetailsHeading('documentLetter');
    cy.get(entityDetailsBackButtonSelector).click({ force: true });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', documentLetterPageUrlPattern);
  });

  it('should load create DocumentLetter page', () => {
    cy.visit(documentLetterPageUrl);
    cy.wait('@entitiesRequest');
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('DocumentLetter');
    cy.get(entityCreateSaveButtonSelector).should('exist');
    cy.get(entityCreateCancelButtonSelector).click({ force: true });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', documentLetterPageUrlPattern);
  });

  it('should load edit DocumentLetter page', function () {
    cy.visit(documentLetterPageUrl);
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        this.skip();
      }
    });
    cy.get(entityEditButtonSelector).first().click({ force: true });
    cy.getEntityCreateUpdateHeading('DocumentLetter');
    cy.get(entityCreateSaveButtonSelector).should('exist');
    cy.get(entityCreateCancelButtonSelector).click({ force: true });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', documentLetterPageUrlPattern);
  });

  it('should create an instance of DocumentLetter', () => {
    cy.visit(documentLetterPageUrl);
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('DocumentLetter');

    cy.get(`[data-cy="remoteId"]`).type('61144').should('have.value', '61144');

    cy.get(`[data-cy="contactName"]`).type('synthesize').should('have.value', 'synthesize');

    cy.get(`[data-cy="date"]`).type('2021-08-16').should('have.value', '2021-08-16');

    cy.get(`[data-cy="title"]`).type('AI Plain Shore').should('have.value', 'AI Plain Shore');

    cy.get(`[data-cy="content"]`).type('Nebraska Focused').should('have.value', 'Nebraska Focused');

    cy.get(`[data-cy="language"]`).select('ENGLISH');

    cy.get(`[data-cy="pageAmount"]`).type('94256').should('have.value', '94256');

    cy.get(`[data-cy="notes"]`).type('Nauru').should('have.value', 'Nauru');

    cy.get(`[data-cy="status"]`).select('DRAFT');

    cy.get(`[data-cy="created"]`).type('2021-08-16T20:26').should('have.value', '2021-08-16T20:26');

    cy.setFieldSelectToLastOfEntity('contact');

    cy.setFieldSelectToLastOfEntity('contactAddress');

    cy.setFieldSelectToLastOfEntity('contactPerson');

    cy.setFieldSelectToLastOfEntity('contactPrePageAddress');

    cy.setFieldSelectToLastOfEntity('layout');

    cy.setFieldSelectToLastOfEntity('signature');

    cy.get(entityCreateSaveButtonSelector).click({ force: true });
    cy.scrollTo('top', { ensureScrollable: false });
    cy.get(entityCreateSaveButtonSelector).should('not.exist');
    cy.wait('@postEntityRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(201);
    });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', documentLetterPageUrlPattern);
  });

  it('should delete last instance of DocumentLetter', function () {
    cy.visit(documentLetterPageUrl);
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length > 0) {
        cy.get(entityTableSelector).should('have.lengthOf', response.body.length);
        cy.get(entityDeleteButtonSelector).last().click({ force: true });
        cy.getEntityDeleteDialogHeading('documentLetter').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', documentLetterPageUrlPattern);
      } else {
        this.skip();
      }
    });
  });
});
