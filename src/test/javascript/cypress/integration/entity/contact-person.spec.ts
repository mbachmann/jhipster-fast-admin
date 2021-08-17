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

describe('ContactPerson e2e test', () => {
  const contactPersonPageUrl = '/contact-person';
  const contactPersonPageUrlPattern = new RegExp('/contact-person(\\?.*)?$');
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
    cy.intercept('GET', '/api/contact-people+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/contact-people').as('postEntityRequest');
    cy.intercept('DELETE', '/api/contact-people/*').as('deleteEntityRequest');
  });

  it('should load ContactPeople', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('contact-person');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('ContactPerson').should('exist');
    cy.url().should('match', contactPersonPageUrlPattern);
  });

  it('should load details ContactPerson page', function () {
    cy.visit(contactPersonPageUrl);
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        this.skip();
      }
    });
    cy.get(entityDetailsButtonSelector).first().click({ force: true });
    cy.getEntityDetailsHeading('contactPerson');
    cy.get(entityDetailsBackButtonSelector).click({ force: true });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', contactPersonPageUrlPattern);
  });

  it('should load create ContactPerson page', () => {
    cy.visit(contactPersonPageUrl);
    cy.wait('@entitiesRequest');
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('ContactPerson');
    cy.get(entityCreateSaveButtonSelector).should('exist');
    cy.get(entityCreateCancelButtonSelector).click({ force: true });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', contactPersonPageUrlPattern);
  });

  it('should load edit ContactPerson page', function () {
    cy.visit(contactPersonPageUrl);
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        this.skip();
      }
    });
    cy.get(entityEditButtonSelector).first().click({ force: true });
    cy.getEntityCreateUpdateHeading('ContactPerson');
    cy.get(entityCreateSaveButtonSelector).should('exist');
    cy.get(entityCreateCancelButtonSelector).click({ force: true });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', contactPersonPageUrlPattern);
  });

  it('should create an instance of ContactPerson', () => {
    cy.visit(contactPersonPageUrl);
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('ContactPerson');

    cy.get(`[data-cy="remoteId"]`).type('54183').should('have.value', '54183');

    cy.get(`[data-cy="defaultPerson"]`).should('not.be.checked');
    cy.get(`[data-cy="defaultPerson"]`).click().should('be.checked');

    cy.get(`[data-cy="name"]`).type('Plastic Frozen Loan').should('have.value', 'Plastic Frozen Loan');

    cy.get(`[data-cy="surname"]`).type('Avon tan').should('have.value', 'Avon tan');

    cy.get(`[data-cy="gender"]`).select('FEMALE');

    cy.get(`[data-cy="email"]`).type('Gunner55@gmail.com').should('have.value', 'Gunner55@gmail.com');

    cy.get(`[data-cy="phone"]`).type('1-257-231-4558').should('have.value', '1-257-231-4558');

    cy.get(`[data-cy="department"]`).type('Industrial').should('have.value', 'Industrial');

    cy.get(`[data-cy="salutation"]`).type('Georgia Riel').should('have.value', 'Georgia Riel');

    cy.get(`[data-cy="showTitle"]`).should('not.be.checked');
    cy.get(`[data-cy="showTitle"]`).click().should('be.checked');

    cy.get(`[data-cy="showDepartment"]`).should('not.be.checked');
    cy.get(`[data-cy="showDepartment"]`).click().should('be.checked');

    cy.get(`[data-cy="wantsNewsletter"]`).should('not.be.checked');
    cy.get(`[data-cy="wantsNewsletter"]`).click().should('be.checked');

    cy.get(`[data-cy="birthDate"]`).type('2021-08-17').should('have.value', '2021-08-17');

    cy.get(`[data-cy="birthPlace"]`).type('convergence Supervisor Inverse').should('have.value', 'convergence Supervisor Inverse');

    cy.get(`[data-cy="placeOfOrigin"]`).type('end-to-end Web conglomeration').should('have.value', 'end-to-end Web conglomeration');

    cy.get(`[data-cy="citizenCountry1"]`).type('Bedfordshire capacitor Gorgeous').should('have.value', 'Bedfordshire capacitor Gorgeous');

    cy.get(`[data-cy="citizenCountry2"]`).type('Soap Branding').should('have.value', 'Soap Branding');

    cy.get(`[data-cy="socialSecurityNumber"]`).type('Division Forward Egypt').should('have.value', 'Division Forward Egypt');

    cy.get(`[data-cy="hobbies"]`).type('Specialist bandwidth').should('have.value', 'Specialist bandwidth');

    cy.get(`[data-cy="dailyWork"]`).type('synthesize').should('have.value', 'synthesize');

    cy.get(`[data-cy="contactAttribute01"]`).type('infrastructure olive Rubber').should('have.value', 'infrastructure olive Rubber');

    cy.setFieldImageAsBytesOfEntity('avatar', 'integration-test.png', 'image/png');

    cy.get(`[data-cy="imageType"]`).type('UAE Centers bandwidth-monitored').should('have.value', 'UAE Centers bandwidth-monitored');

    cy.get(`[data-cy="inactiv"]`).should('not.be.checked');
    cy.get(`[data-cy="inactiv"]`).click().should('be.checked');

    cy.setFieldSelectToLastOfEntity('contact');

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
    cy.url().should('match', contactPersonPageUrlPattern);
  });

  it('should delete last instance of ContactPerson', function () {
    cy.visit(contactPersonPageUrl);
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length > 0) {
        cy.get(entityTableSelector).should('have.lengthOf', response.body.length);
        cy.get(entityDeleteButtonSelector).last().click({ force: true });
        cy.getEntityDeleteDialogHeading('contactPerson').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', contactPersonPageUrlPattern);
      } else {
        this.skip();
      }
    });
  });
});
