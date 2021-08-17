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

describe('DocumentPosition e2e test', () => {
  const documentPositionPageUrl = '/document-position-fa';
  const documentPositionPageUrlPattern = new RegExp('/document-position-fa(\\?.*)?$');
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
    cy.intercept('GET', '/api/document-positions+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/document-positions').as('postEntityRequest');
    cy.intercept('DELETE', '/api/document-positions/*').as('deleteEntityRequest');
  });

  it('should load DocumentPositions', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('document-position-fa');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('DocumentPosition').should('exist');
    cy.url().should('match', documentPositionPageUrlPattern);
  });

  it('should load details DocumentPosition page', function () {
    cy.visit(documentPositionPageUrl);
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        this.skip();
      }
    });
    cy.get(entityDetailsButtonSelector).first().click({ force: true });
    cy.getEntityDetailsHeading('documentPosition');
    cy.get(entityDetailsBackButtonSelector).click({ force: true });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', documentPositionPageUrlPattern);
  });

  it('should load create DocumentPosition page', () => {
    cy.visit(documentPositionPageUrl);
    cy.wait('@entitiesRequest');
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('DocumentPosition');
    cy.get(entityCreateSaveButtonSelector).should('exist');
    cy.get(entityCreateCancelButtonSelector).click({ force: true });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', documentPositionPageUrlPattern);
  });

  it('should load edit DocumentPosition page', function () {
    cy.visit(documentPositionPageUrl);
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        this.skip();
      }
    });
    cy.get(entityEditButtonSelector).first().click({ force: true });
    cy.getEntityCreateUpdateHeading('DocumentPosition');
    cy.get(entityCreateSaveButtonSelector).should('exist');
    cy.get(entityCreateCancelButtonSelector).click({ force: true });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', documentPositionPageUrlPattern);
  });

  it('should create an instance of DocumentPosition', () => {
    cy.visit(documentPositionPageUrl);
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('DocumentPosition');

    cy.get(`[data-cy="type"]`).select('TEXT');

    cy.get(`[data-cy="catalogType"]`).select('ALL');

    cy.get(`[data-cy="number"]`).type('Consultant').should('have.value', 'Consultant');

    cy.get(`[data-cy="name"]`).type('Profound back-end').should('have.value', 'Profound back-end');

    cy.get(`[data-cy="description"]`).type('Account').should('have.value', 'Account');

    cy.get(`[data-cy="price"]`).type('68742').should('have.value', '68742');

    cy.get(`[data-cy="vat"]`).type('84924').should('have.value', '84924');

    cy.get(`[data-cy="amount"]`).type('62500').should('have.value', '62500');

    cy.get(`[data-cy="discountRate"]`).type('18419').should('have.value', '18419');

    cy.get(`[data-cy="discountType"]`).select('AMOUNT');

    cy.get(`[data-cy="total"]`).type('87180').should('have.value', '87180');

    cy.get(`[data-cy="showOnlyTotal"]`).should('not.be.checked');
    cy.get(`[data-cy="showOnlyTotal"]`).click().should('be.checked');

    cy.setFieldSelectToLastOfEntity('unit');

    cy.setFieldSelectToLastOfEntity('deliveryNote');

    cy.setFieldSelectToLastOfEntity('invoice');

    cy.setFieldSelectToLastOfEntity('offer');

    cy.setFieldSelectToLastOfEntity('orderConfirmation');

    cy.get(entityCreateSaveButtonSelector).click({ force: true });
    cy.scrollTo('top', { ensureScrollable: false });
    cy.get(entityCreateSaveButtonSelector).should('not.exist');
    cy.wait('@postEntityRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(201);
    });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', documentPositionPageUrlPattern);
  });

  it('should delete last instance of DocumentPosition', function () {
    cy.visit(documentPositionPageUrl);
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length > 0) {
        cy.get(entityTableSelector).should('have.lengthOf', response.body.length);
        cy.get(entityDeleteButtonSelector).last().click({ force: true });
        cy.getEntityDeleteDialogHeading('documentPosition').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', documentPositionPageUrlPattern);
      } else {
        this.skip();
      }
    });
  });
});
