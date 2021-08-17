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

describe('CustomFieldValue e2e test', () => {
  const customFieldValuePageUrl = '/custom-field-value';
  const customFieldValuePageUrlPattern = new RegExp('/custom-field-value(\\?.*)?$');
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
    cy.intercept('GET', '/api/custom-field-values+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/custom-field-values').as('postEntityRequest');
    cy.intercept('DELETE', '/api/custom-field-values/*').as('deleteEntityRequest');
  });

  it('should load CustomFieldValues', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('custom-field-value');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('CustomFieldValue').should('exist');
    cy.url().should('match', customFieldValuePageUrlPattern);
  });

  it('should load details CustomFieldValue page', function () {
    cy.visit(customFieldValuePageUrl);
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        this.skip();
      }
    });
    cy.get(entityDetailsButtonSelector).first().click({ force: true });
    cy.getEntityDetailsHeading('customFieldValue');
    cy.get(entityDetailsBackButtonSelector).click({ force: true });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', customFieldValuePageUrlPattern);
  });

  it('should load create CustomFieldValue page', () => {
    cy.visit(customFieldValuePageUrl);
    cy.wait('@entitiesRequest');
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('CustomFieldValue');
    cy.get(entityCreateSaveButtonSelector).should('exist');
    cy.get(entityCreateCancelButtonSelector).click({ force: true });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', customFieldValuePageUrlPattern);
  });

  it('should load edit CustomFieldValue page', function () {
    cy.visit(customFieldValuePageUrl);
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        this.skip();
      }
    });
    cy.get(entityEditButtonSelector).first().click({ force: true });
    cy.getEntityCreateUpdateHeading('CustomFieldValue');
    cy.get(entityCreateSaveButtonSelector).should('exist');
    cy.get(entityCreateCancelButtonSelector).click({ force: true });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', customFieldValuePageUrlPattern);
  });

  it('should create an instance of CustomFieldValue', () => {
    cy.visit(customFieldValuePageUrl);
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('CustomFieldValue');

    cy.get(`[data-cy="key"]`).type('aggregate protocol').should('have.value', 'aggregate protocol');

    cy.get(`[data-cy="name"]`).type('neural').should('have.value', 'neural');

    cy.get(`[data-cy="value"]`).type('Tennessee').should('have.value', 'Tennessee');

    cy.setFieldSelectToLastOfEntity('customField');

    cy.setFieldSelectToLastOfEntity('contact');

    cy.setFieldSelectToLastOfEntity('contactPerson');

    cy.setFieldSelectToLastOfEntity('project');

    cy.setFieldSelectToLastOfEntity('catalogProduct');

    cy.setFieldSelectToLastOfEntity('catalogService');

    cy.setFieldSelectToLastOfEntity('documentLetter');

    cy.setFieldSelectToLastOfEntity('deliveryNote');

    cy.get(entityCreateSaveButtonSelector).click({ force: true });
    cy.scrollTo('top', { ensureScrollable: false });
    cy.get(entityCreateSaveButtonSelector).should('not.exist');
    cy.wait('@postEntityRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(201);
    });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', customFieldValuePageUrlPattern);
  });

  it('should delete last instance of CustomFieldValue', function () {
    cy.visit(customFieldValuePageUrl);
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length > 0) {
        cy.get(entityTableSelector).should('have.lengthOf', response.body.length);
        cy.get(entityDeleteButtonSelector).last().click({ force: true });
        cy.getEntityDeleteDialogHeading('customFieldValue').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', customFieldValuePageUrlPattern);
      } else {
        this.skip();
      }
    });
  });
});
